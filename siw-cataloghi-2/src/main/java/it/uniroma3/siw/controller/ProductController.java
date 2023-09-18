package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Product;
import it.uniroma3.siw.model.Supplier;
import it.uniroma3.siw.repository.ProductRepository;
import it.uniroma3.siw.repository.SupplierRepository;
import it.uniroma3.siw.service.CommentService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ProductService;
import it.uniroma3.siw.validator.ProductValidator;

@Controller
public class ProductController {

	@Autowired ProductRepository productRepository;
	@Autowired SupplierRepository supplierRepository;
	@Autowired ProductValidator productValidator;
	@Autowired ProductService productService;
	@Autowired CredentialsService credentialsService;
	@Autowired CommentService commentService;
	
	@GetMapping("/product/{idProduct}")
	public String seeProduct(@PathVariable("idProduct") Long id, Model model) {
		Product product=this.productRepository.findById(id).get();
		UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials=credentialsService.getCredentials(userDetails.getUsername());
		model.addAttribute("product", product);
		model.addAttribute("userComments", this.commentService.commentsByCredentials(credentials, product.getComments()));
		model.addAttribute("comments", this.commentService.commentsNotByCredentials(credentials, product.getComments()));
		return "product.html";
	}
	
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
		Product newProduct=new Product();
		newProduct.setName(product.getName());
		newProduct.setPrice(product.getPrice());
		newProduct.setDescription(product.getDescription());
		model.addAttribute("product", product);
		model.addAttribute("newProduct", newProduct);
		return "formUpdateInfo.html";
	}
	
	@PostMapping("/updateProductInfo/{idProduct}") 
	public String updateProductName(@ModelAttribute("newProduct") Product product, BindingResult bindingResult,
			@PathVariable("idProduct") Long idProduct, Model model) {
		this.productValidator.validate(product, bindingResult);
		Product found=this.productRepository.findById(idProduct).get();
		UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials=credentialsService.getCredentials(userDetails.getUsername());
		if(!bindingResult.hasErrors()) {
			found.setName(product.getName());
			found.setDescription(product.getDescription());
			found.setPrice(product.getPrice());
			this.productRepository.save(found);
			model.addAttribute("product",found);
			model.addAttribute("userComments", this.commentService.commentsByCredentials(credentials, found.getComments()));
			model.addAttribute("comments", this.commentService.commentsNotByCredentials(credentials, found.getComments()));
			return "product.html";
		}
		model.addAttribute("messaggioErrore", "Assicurati di aggiungere sia un nome che un prezzo validi");
		return "formUpdateInfo.html";
	}
	
	@GetMapping("/formSearchProduct")
	public String searchProducts(Model model) {
		return "formSearchProduct.html";
	}
	
	@GetMapping("/formSearchProductByName")
	public String orderByName(Model model) {
		List<Product> products=this.productRepository.findByOrderByNameAsc();
		model.addAttribute("products", products);
		return "products.html";
	}
	
	@GetMapping("/formSearchProductByPrice")
	public String orderByPrice(Model model) {
		List<Product> products=this.productRepository.findByOrderByPriceAsc();
		model.addAttribute("products", products);
		return "productsByPrice.html";
	}
	
	@GetMapping("/formSearchBySupplier") 
	public String supplierLists(Model model) {
		model.addAttribute("suppliers", this.supplierRepository.findAll());
		return "supplierList.html";
	}
	
	@GetMapping("/orderBySupplier/{id}")
	public String orderBySupplier(@PathVariable("id") Long id, Model model) {
		Supplier supplier=this.supplierRepository.findById(id).get();
		List<Product> products=this.productRepository.findBySuppliersContaining(supplier);
		model.addAttribute("products", products);
		return "products.html";
	}
	
	@GetMapping("/products") 
	public String allProducts(Model model) {
		model.addAttribute("products", this.productRepository.findAll());
		return "products.html";
	}
}
