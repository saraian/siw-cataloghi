package it.uniroma3.siw.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.UserRepository;

import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

	@Autowired UserRepository userRepository;
	
	@Override
	public void validate(Object object, Errors errors) {
		User user=(User) object;
		if(user.getEmail()!=null&&userRepository.existsByEmail(user.getEmail())) {
			errors.reject("email.duplicate");
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return User.class.equals(aClass);
	}
}
