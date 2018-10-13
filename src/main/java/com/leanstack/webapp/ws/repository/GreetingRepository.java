package com.leanstack.webapp.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leanstack.webapp.ws.model.Greeting;

@Repository
public interface GreetingRepository extends JpaRepository<Greeting, Long>{
	
}
