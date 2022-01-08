package com.example.medrest.repository;

import com.example.medrest.model.Specialisation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialisationRepository extends CrudRepository<Specialisation, Long> {

}
