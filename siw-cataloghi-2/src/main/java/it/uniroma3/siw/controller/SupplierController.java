package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Supplier;
import it.uniroma3.siw.repository.SupplierRepository;
import it.uniroma3.siw.validator.SupplierValidator;

@Controller
public class SupplierController {
	
	@Autowired SupplierValidator supplierValidator;
	@Autowired SupplierRepository supplierRepository;
	
	@GetMapping("/formNewSupplier")
	public String formNewSupplier(Model model) {
		model.addAttribute("supplier", new Supplier());
		return "formNewSupplier.html";
	}
	
	@PostMapping("/formNewSupplier")
	public String saveNewSupplier(@ModelAttribute("supplier") Supplier supplier, BindingResult bindingResults, Model model) {
		supplierValidator.validate(supplier, bindingResults);
		if(!bindingResults.hasFieldErrors()) {
			this.supplierRepository.save(supplier);
			model.addAttribute("supplier", supplier);
			return "index.html";
		}
		else {
			model.addAttribute("messaggioErrore", "Questo fornitore è già registrato nel catalogo");
			return "formNewSupplier.html";
		}
	}
}