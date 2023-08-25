package it.uniroma3.siw.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.repository.CredentialsRepository;

import org.springframework.validation.Validator;

@Component
public class CredentialsValidator implements Validator {

	@Autowired CredentialsRepository credentialsRepository;
	
	@Override
	public void validate(Object object, Errors errors) {
		Credentials credentials=(Credentials) object;
		if(credentials.getUsername()!=null&&credentialsRepository.existsByUsername(credentials.getUsername())) {
			errors.reject("username.duplicate");
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return Credentials.class.equals(aClass);
	}
}
