package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired UserRepository userRepository;
	
	public User getUser(Long id) {
		User user=this.userRepository.findById(id).get();
		return user;
	}
	
	public User saveUser(User user) {
		User saved=this.userRepository.save(user);
		return saved;
	}
}
