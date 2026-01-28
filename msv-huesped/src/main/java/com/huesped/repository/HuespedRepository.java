package com.huesped.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.huesped.entity.Huesped;

@Repository
public interface HuespedRepository extends JpaRepository<Huesped, Long>{

}
