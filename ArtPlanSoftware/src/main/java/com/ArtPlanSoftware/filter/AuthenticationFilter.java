package com.ArtPlanSoftware.filter;

import com.ArtPlanSoftware.entity.UserAttemptsEntity;
import com.ArtPlanSoftware.entity.UserEntity;
import com.ArtPlanSoftware.service.impl.UserAttemptsServiceImpl;
import com.ArtPlanSoftware.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final long HOUR = 3600000;

    private final AuthenticationManager authenticationManager;
    @Autowired
    private final UserAttemptsServiceImpl userAttemptsService;
    @Autowired
    private final UserServiceImpl userService;

    public AuthenticationFilter(AuthenticationManager authenticationManager, UserAttemptsServiceImpl userAttemptsService,
                                UserServiceImpl userService)
    {
        this.authenticationManager = authenticationManager;
        this.userAttemptsService = userAttemptsService;
        this.userService = userService;
        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UserEntity requestUser = new ObjectMapper().readValue(request.getInputStream(), UserEntity.class);
            UserEntity user = userService.getUserByUsername(requestUser.getUsername());
            if (user == null) {
                throw new BadCredentialsException("User with username " + "'" + "'" + " not found!");
            }
            if (user.isAccountLocked()) {
                UserAttemptsEntity attempts = userAttemptsService.getAttempts(user.getUsername());
                if (new Date().getTime() - attempts.getLastModified().getTime() < HOUR) {
                    throw new LockedException("User with username " + "'" + user.getUsername() + "'" + " was locked!");
                } else {
                    user.setAccountLocked(false);
                    userService.saveUser(user);
                }
            }
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestUser.getUsername(),
                    requestUser.getPassword(), new ArrayList<>()));
        }
        catch (IOException e) {
            throw new RuntimeException("Could not read request" + e);
        }
    }

    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain filterChain,
                                            Authentication authentication) {
        String token = Jwts.builder()
                .setSubject(((User) authentication.getPrincipal()).getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 864_000_000))
                .signWith(SignatureAlgorithm.HS512, "SecretKeyToGenJWTs".getBytes())
                .compact();
        response.addHeader("Authorization","Bearer " + token);
        userAttemptsService.resetAttempts(((User) authentication.getPrincipal()).getUsername());
    }
}