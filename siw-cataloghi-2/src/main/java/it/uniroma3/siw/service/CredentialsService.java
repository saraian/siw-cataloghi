package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.repository.CredentialsRepository;

@Service
public class CredentialsService {
	@Autowired CredentialsRepository credentialsRepository;
	
	public Credentials getCredentials(Long id) {
		Credentials credentials=this.credentialsRepository.findById(id).get();
		return credentials;
	}
	
	public Credentials getCredentials(String username) {
		Credentials credentials=this.credentialsRepository.findByUsername(username).get();
		return credentials;
	}
	
	public Credentials saveCredentials(Credentials credentials) {
		credentials.setRole(Credentials.DEFAULT);
		Credentials saved=this.credentialsRepository.save(credentials);
		return saved;
	}
}
