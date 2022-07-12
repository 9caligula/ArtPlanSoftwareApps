package com.ArtPlanSoftware.filter;

import com.ArtPlanSoftware.response.ResponseError;
import com.ArtPlanSoftware.service.impl.UserAttemptsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEventListener {

    @Autowired
    private UserAttemptsServiceImpl userAttemptsService;

    @EventListener
    public ResponseEntity<?> authenticationFailed(AuthenticationFailureBadCredentialsEvent event) {
        try {
            String username = (String) event.getAuthentication().getPrincipal();
            userAttemptsService.updateAttempts(username);
            return null;
        } catch (LockedException e) {
            ResponseError responseError = new ResponseError(e.getMessage(), HttpStatus.CONFLICT);
            return new ResponseEntity<>(responseError, HttpStatus.CONFLICT);
        } catch (BadCredentialsException e) {
            ResponseError responseError = new ResponseError(e.getMessage(), HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(responseError, HttpStatus.NOT_FOUND);
        }
    }
}