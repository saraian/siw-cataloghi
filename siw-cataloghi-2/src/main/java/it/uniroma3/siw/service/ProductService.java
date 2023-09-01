package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Product;
import it.uniroma3.siw.model.Supplier;
import it.uniroma3.siw.repository.ProductRepository;
import it.uniroma3.siw.repository.SupplierRepository;

@Service
public class ProductService {
	
	@Autowired ProductRepository productRepository;
	@Autowired SupplierRepository supplierRepository;
	
	public List<Supplier> findPotentialSupplier(Product product) {
		List<Supplier> potentialSuppliers=new ArrayList<Supplier>();
		List<Supplier> currentSuppliers=product.getSuppliers();
		Iterator<Supplier> allSuppliers=supplierRepository.findAll().iterator();
		while(allSuppliers.hasNext()) {
			Supplier supplier=allSuppliers.next();
			if(!currentSuppliers.contains(supplier))
				potentialSuppliers.add(supplier);
		}
		return potentialSuppliers;
	}
}
