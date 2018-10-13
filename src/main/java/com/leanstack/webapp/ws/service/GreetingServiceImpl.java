package com.leanstack.webapp.ws.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leanstack.webapp.ws.model.Greeting;
import com.leanstack.webapp.ws.repository.GreetingRepository;

@Service
public class GreetingServiceImpl implements GreetingService{			

	@Autowired
	private GreetingRepository greetingRepository;
	
	@Override
	public Collection<Greeting> findAll() {
		Collection<Greeting> greetings = greetingRepository.findAll();
		return greetings;
	}

	@Override
	public Optional<Greeting> findOne(Long id) {
		Optional<Greeting> greeting = greetingRepository.findById(id);
		return greeting;
	}

	@Override
	public Greeting create(Greeting greeting) {
		
		if(greeting.getId() != null) {
			//Cannot Create greeting with specific greeting value
			return null;
		}
		
		Greeting savedGreeting = greetingRepository.save(greeting);
		return savedGreeting;
	}

	@Override
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
	public void delete(Long id) {
		greetingRepository.deleteById(id);		
	}

}
