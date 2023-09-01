package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Product;
import it.uniroma3.siw.model.Supplier;
import it.uniroma3.siw.repository.ProductRepository;
import it.uniroma3.siw.repository.SupplierRepository;
import it.uniroma3.siw.service.ProductService;
import it.uniroma3.siw.validator.ProductValidator;

@Controller
public class ProductController {

	@Autowired ProductRepository productRepository;
	@Autowired SupplierRepository supplierRepository;
	@Autowired ProductValidator productValidator;
	@Autowired ProductService productService;
	
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
	
	@GetMapping("/formUpdateProduct/{idProduct}")
	public String chooseUpdate(@PathVariable("idProduct") Long idProduct, Model model) {
		Product product=productRepository.findById(idProduct).get();
		model.addAttribute("product", product);
		return "formUpdateProduct.html";
	}
	
	@GetMapping("/updateSupplierForProduct/{idProduct}")
	public String updateSupplier(@PathVariable("idProduct") Long idProduct, Model model) {
		Product product=this.productRepository.findById(idProduct).get();
		List<Supplier> currentSuppliers=product.getSuppliers();
		model.addAttribute("product", product);
		model.addAttribute("currentSuppliers", currentSuppliers);
		model.addAttribute("newSuppliers", productService.findPotentialSupplier(product));
		return "addSuppliers.html";
	}
	
	@PostMapping("/updateSupplierForProduct/{idProduct}/{idSupplier}")
	public String updateSupplier(@PathVariable("idProduct") Long idProduct, @PathVariable("idSupplier") Long idSupplier, Model model) {
		Product product=this.productRepository.findById(idProduct).get();
		Supplier newSupplier=this.supplierRepository.findById(idSupplier).get();
		List<Supplier> suppliers=product.getSuppliers();
		if(suppliers.contains(newSupplier))
			suppliers.remove(newSupplier);
		else
			suppliers.add(newSupplier);
		this.productRepository.save(product);
		model.addAttribute("product", product);
		model.addAttribute("currentSuppliers", suppliers);
		model.addAttribute("newSuppliers", productService.findPotentialSupplier(product));
		return "addSuppliers.html";
	}
	
	@PostMapping("/deleteProduct/{idProduct}")
	public String deleteProduct(@PathVariable("idProduct") Long idProduct, Model model) {
		this.productRepository.deleteById(idProduct);
		model.addAttribute("products", this.productRepository.findAll());
		return "manageProduct.html";
	}
	
	@GetMapping("/updateProductInfo/{idProduct}")
	public String updateProduct(@PathVariable("idProduct") Long idProduct, Model model) {
		Product product=this.productRepository.findById(idProduct).get();
		model.addAttribute("product", product);
		model.addAttribute("name", new String());
		model.addAttribute("description", new String());
		model.addAttribute("price", 0);
		return "formUpdateInfo.html";
	}
	
	@PostMapping("/updateName/{idProduct}") 
	public String updateProductName(@ModelAttribute("name") String name, @PathVariable("idProduct") Long idProduct, Model model) {
		Product product=this.productRepository.findById(idProduct).get();
		product.setName(name);
		this.productRepository.save(product);
		model.addAttribute("product",product);
		return "product.html";
	}
	
	@PostMapping("/updateDescription/{idProduct}") 
	public String updateProductDescription(@PathVariable("idProduct") Long idProduct, @ModelAttribute("description") String description, Model model) {
		Product product=this.productRepository.findById(idProduct).get();
		product.setDescription(description);
		this.productRepository.save(product);
		model.addAttribute("product",product);
		return "product.html";
	}
	
	@PostMapping("/updatePrice/{idProduct}") 
	public String updateProductPrice(@PathVariable("idProduct") Long idProduct, @ModelAttribute("price") Float price, Model model) {
		Product product=this.productRepository.findById(idProduct).get();
		if(price>0) {
			product.setPrice(price);
			this.productRepository.save(product);
		}
		model.addAttribute("product",product);
		return "product.html";
	}
}
