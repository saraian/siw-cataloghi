package it.uniroma3.siw.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Product;
import it.uniroma3.siw.repository.ProductRepository;

public class ProductValidator implements Validator{
	
	@Autowired ProductRepository productRepository;
	
	@Override
	public void validate(Object object, Errors errors) {
		Product product=(Product) object;
		if(product.getName()!=null&&productRepository.existsByName(product.getName())) {
			errors.reject("product.duplicate");
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return Product.class.equals(aClass);
	}
}
