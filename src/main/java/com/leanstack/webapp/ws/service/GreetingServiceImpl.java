package com.leanstack.webapp.ws.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.leanstack.webapp.ws.model.Greeting;
import com.leanstack.webapp.ws.repository.GreetingRepository;

@Service
@Transactional(
		propagation = Propagation.SUPPORTS,
		readOnly = true
)
public class GreetingServiceImpl implements GreetingService{			

	@Autowired
	private GreetingRepository greetingRepository;
	
	@Override
	public Collection<Greeting> findAll() {
		Collection<Greeting> greetings = greetingRepository.findAll();
		return greetings;
	}

	@Override
	@Cacheable(
			value = "greetings",
			key = "#id")
	public Optional<Greeting> findOne(Long id) {
		Optional<Greeting> greeting = greetingRepository.findById(id);
		return greeting;
	}

	@Override
	@Transactional(
			propagation = Propagation.REQUIRED, 
			readOnly = false)
	@CachePut(
			value = "greetings",
			key = "#result.id")
	public Greeting create(Greeting greeting) {
		
		if(greeting.getId() != null) {
			//Cannot Create greeting with specific greeting value
			return null;
		}
		
		Greeting savedGreeting = greetingRepository.save(greeting);
		
		//Illustration of Roll back Scenario
		if(savedGreeting.getId() == 4) {
			throw new RuntimeException("Rollback Scenario");
		}
		
		
		return savedGreeting;
	}

	@Override
	@Transactional(
			propagation = Propagation.REQUIRED, 
			readOnly = false)
	@CachePut(
			value = "greetings",
			key = "#greeting.id")
	public Greeting update(Greeting greeting) {
			
		Optional<Greeting> persitedGreeting = greetingRepository.findById(greeting.getId());
		if(persitedGreeting == null) {
			//Cannot update as supplied greeting is present in DB
			return null;
		}
		
		Greeting updatedGreeting = greetingRepository.save(greeting);
		return updatedGreeting;
	}

	@Override
	@Transactional(
			propagation = Propagation.REQUIRED, 
			readOnly = false)
	@CacheEvict(
			value = "greetings",
			key = "#id")
	public void delete(Long id) {
		greetingRepository.deleteById(id);		
	}

	@Override
	@CacheEvict(
			value = "greetings",
			allEntries = true)
	public void evitCache() {
		
	}

}
