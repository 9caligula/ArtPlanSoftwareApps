package com.ArtPlanSoftware.service;

import com.ArtPlanSoftware.entity.PetEntity;
import com.ArtPlanSoftware.entity.TypeOfPetsEntity;

import java.util.List;
import java.util.Optional;

public interface PetService {

    PetEntity createPet(Long typeId, PetEntity pet);

    TypeOfPetsEntity createType(TypeOfPetsEntity type);

    PetEntity updatePet(PetEntity pet);

    void deletePet(Long id);

    List<PetEntity> getAllMyPets();

    Optional<PetEntity> getPetById(Long id);
}
