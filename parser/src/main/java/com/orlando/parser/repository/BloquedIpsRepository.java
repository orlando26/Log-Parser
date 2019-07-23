package com.orlando.parser.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orlando.parser.model.BloquedIps;

public interface BloquedIpsRepository extends JpaRepository<BloquedIps, Integer>{

}
