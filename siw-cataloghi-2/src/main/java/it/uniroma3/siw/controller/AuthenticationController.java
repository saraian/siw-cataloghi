package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.AuthConfiguration;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.UserService;
import it.uniroma3.siw.validator.CredentialsValidator;
import it.uniroma3.siw.validator.UserValidator;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {

	@Autowired CredentialsService credentialsService;
	@Autowired UserService userService;
	@Autowired AuthConfiguration authConfiguration;
	@Autowired CredentialsValidator credentialsValidator;
	@Autowired UserValidator userValidator;
	
	@GetMapping(value="/")
	public String index(Model model) {
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		if(authentication instanceof AnonymousAuthenticationToken)
			return "userIndex.html";
		else {
			UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials=credentialsService.getCredentials(userDetails.getUsername());
			if(credentials.getRole().equals(Credentials.ADMIN_ROLE))
				return "index.html";
		}
		return "userIndex.html";
	}
	
	@GetMapping(value="/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		return "formRegisterUser.html";
	}
	
	@PostMapping(value="/register") 
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult userBindingResult, 
			@Valid @ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult, Model model) {
		credentialsValidator.validate(credentials, credentialsBindingResult);
		userValidator.validate(user, userBindingResult);
		if(!userBindingResult.hasErrors()&&!credentialsBindingResult.hasErrors()) {
			PasswordEncoder encoder=authConfiguration.passwordEncoder();
			String password=credentials.getPassword();
			credentials.setPassword(encoder.encode(password));
			User saved=userService.saveUser(user);
			credentials.setUser(saved);
			credentialsService.saveCredentials(credentials);
			return "login.html";
		}
		model.addAttribute("messaggioErrore", "Username o e-email già in uso");
		return "formRegisterUser.html";
	}
	
	@GetMapping(value="/login") 
	public String getLoginPage(Model model) {
		return "login.html";
	}
	
	@GetMapping(value="/login?error=true") 
	public String loginError(Model model) {
		return "login.html";
	}
	
	@GetMapping(value="/success")
	public String afterLogin(Model model) {
		UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials=credentialsService.getCredentials(userDetails.getUsername());
		if(credentials.getRole().equals(Credentials.ADMIN_ROLE))
			return "index.html";
		else 
			return "userIndex.html";
	}
	
	@GetMapping(value="/getLogout")
	public String logout(Model model) {
		return "logoutPage.html";
	}
}
