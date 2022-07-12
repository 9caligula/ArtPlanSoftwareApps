package com.ArtPlanSoftware.service.impl;

import com.ArtPlanSoftware.entity.PetEntity;
import com.ArtPlanSoftware.entity.TypeOfPetsEntity;
import com.ArtPlanSoftware.entity.UserEntity;
import com.ArtPlanSoftware.exception.UserAlreadyExistsException;
import com.ArtPlanSoftware.repository.PetRepository;
import com.ArtPlanSoftware.repository.TypeOfPetsRepository;
import com.ArtPlanSoftware.repository.UserRepository;
import com.ArtPlanSoftware.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl implements PetService {

    @Autowired
    private PetRepository petRepository;
    @Autowired
    private TypeOfPetsRepository typeOfPetsRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public PetEntity createPet(Long typeId, PetEntity pet) {
        if (petRepository.existsByName(pet.getName())) {
            throw new UserAlreadyExistsException(String.format("Pet '%s' with such a name already exists!", pet.getName()),
                    HttpStatus.CONFLICT);
        } else {
            String nameUser = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity userEntity = userRepository.findByUsername(nameUser)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            TypeOfPetsEntity type = typeOfPetsRepository.findById(typeId)
                    .orElseThrow(() -> new UsernameNotFoundException("Type not found"));

            pet.setType(type);
            pet.setUser(userEntity);
            return petRepository.save(pet);
        }
    }

    @Override
    public TypeOfPetsEntity createType(TypeOfPetsEntity type) {
        if (typeOfPetsRepository.existsByName(type.getName())) {
            throw new UserAlreadyExistsException(String.format("Type '%s' with such a name already exists!", type.getName()),
                    HttpStatus.CONFLICT);
        } else {
            return typeOfPetsRepository.save(type);
        }
    }

    @Override
    public PetEntity updatePet(PetEntity newPet) {
        if (petRepository.existsByName(newPet.getName())) {
            throw new UserAlreadyExistsException(String.format("Pet '%s' with such a name already exists!", newPet.getName()),
                    HttpStatus.CONFLICT);
        } else {
            if (!userRepository.existsByUsername(newPet.getUser().getUsername())) {
                throw new UsernameNotFoundException("User not found");
            }
            if (!typeOfPetsRepository.existsByName(newPet.getType().getName())) {
                throw new UsernameNotFoundException("Type not found");
            }
        }
        return petRepository.save(newPet);
    }

    @Override
    public void deletePet(Long id) {
        Optional<PetEntity> pet = petRepository.findById(id);
        if (pet.isPresent()) {
            petRepository.delete(pet.get());
        } else {
            throw new UsernameNotFoundException(String.format("Pet with id = %d does not exist", id));
        }
    }

    @Override
    public List<PetEntity> getAllMyPets() {
        String nameUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> userEntity = userRepository.findByUsername(nameUser);
        return petRepository.findAll()
                .stream().filter((pet) -> pet.getUser().getUsername().equals(userEntity.get().getUsername()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PetEntity> getPetById(Long id) {
        return petRepository.findById(id);
    }
}
