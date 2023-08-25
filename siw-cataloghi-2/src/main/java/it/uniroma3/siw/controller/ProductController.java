package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Product;
import it.uniroma3.siw.repository.ProductRepository;
import it.uniroma3.siw.repository.SupplierRepository;
import it.uniroma3.siw.validator.ProductValidator;

@Controller
public class ProductController {

	@Autowired ProductValidator productValidator;
	@Autowired ProductRepository productRepository;
	@Autowired SupplierRepository supplierRepository;
	
	@GetMapping("/formNewProduct")
	public String formNewProduct(Model model) {
		model.addAttribute("product", new Product());
		return "formNewProduct.html";
	}
	
	@PostMapping("/formNewProduct")
	public String saveNewProduct(@ModelAttribute("product") Product product, BindingResult bindingResults, Model model) {
		this.productValidator.validate(product, bindingResults);
		if(!bindingResults.hasErrors()) {
			this.productRepository.save(product);
			model.addAttribute("product", product);
			return "index.html";
		}
		else {
			model.addAttribute("messaggioErrore", "Questo prodotto è già registrato nel sistema");
			return "formNewProduct.html";
		}
	}
	
	@GetMapping("/manageProduct")
	public String getSearchProduct(Model model) {
		model.addAttribute("products", this.productRepository.findAll());
		return "manageProduct.html";
	}
	
	@GetMapping("/updateSupplierForProduct/{idProduct}")
	public String updateSupplier(Model model) {
		return "addSuppliers.html";
	}
	
	@
}
