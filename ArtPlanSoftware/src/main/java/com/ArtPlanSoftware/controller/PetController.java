package com.ArtPlanSoftware.controller;

import com.ArtPlanSoftware.entity.PetEntity;
import com.ArtPlanSoftware.entity.TypeOfPetsEntity;
import com.ArtPlanSoftware.exception.UserAlreadyExistsException;
import com.ArtPlanSoftware.response.ResponseError;
import com.ArtPlanSoftware.response.ResponseSuccess;
import com.ArtPlanSoftware.service.impl.PetServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
public class PetController {

    @Autowired
    private PetServiceImpl petService;

    @PostMapping("/createPet/{typeId}")
    public ResponseEntity<?> createPet(@PathVariable Long typeId, @RequestBody PetEntity pet) {
        try {
            PetEntity createdPet = petService.createPet(typeId, pet);
            return new ResponseEntity<>(createdPet, HttpStatus.CREATED);
        } catch (UsernameNotFoundException e) {
            ResponseError responseError = new ResponseError(e.getMessage(), HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(responseError, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/createType")
    public ResponseEntity<?> createType(@RequestBody TypeOfPetsEntity type) {
        try {
            TypeOfPetsEntity createdType = petService.createType(type);
            return new ResponseEntity<>(createdType, HttpStatus.CREATED);
        }
        catch (UserAlreadyExistsException e) {
            ResponseError responseError = new ResponseError(e.getMessage(), e.getHttpStatus());
            return new ResponseEntity<>(responseError, e.getHttpStatus());
        }
    }

    @PatchMapping("/updatePet")
    public ResponseEntity<?> updatePet(@RequestBody PetEntity pet) {
        try {
            return new ResponseEntity<>(petService.updatePet(pet), HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            ResponseError responseError = new ResponseError(e.getMessage(), HttpStatus.CONFLICT);
            return new ResponseEntity<>(responseError, HttpStatus.CONFLICT);
        }
        catch (UserAlreadyExistsException e) {
            ResponseError responseError = new ResponseError(e.getMessage(), e.getHttpStatus());
            return new ResponseEntity<>(responseError, e.getHttpStatus());
        }
    }

    @GetMapping("/getPet/{id}")
    public ResponseEntity<?> getPet(@PathVariable Long id) {
        return new ResponseEntity<>(petService.getPetById(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePet(@PathVariable Long id) {
        try {
            petService.deletePet(id);
            ResponseSuccess success = new ResponseSuccess(String.format("Pet with id = %d was deleted", id));
            return new ResponseEntity<>(success, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            ResponseError responseError = new ResponseError(e.getMessage(), HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(responseError, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAllMyPets")
    public ResponseEntity<?> getAllMyPets() {
        return new ResponseEntity<>(petService.getAllMyPets(), HttpStatus.OK);
    }
}
