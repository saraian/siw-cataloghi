package it.uniroma3.siw.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Supplier;
import it.uniroma3.siw.repository.SupplierRepository;

@Component
public class SupplierValidator implements Validator{
	
	@Autowired SupplierRepository supplierRepository;
	
	@Override
	public void validate(Object object, Errors errors) {
		Supplier supplier=(Supplier) object;
		if(supplier.getName()!=null&&supplierRepository.existsByName(supplier.getName())) {
			errors.reject("supplier.duplicate");
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return Supplier.class.equals(aClass);
	}
}
