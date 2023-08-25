package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long>{
	
	public boolean existsByName(String name);
}
