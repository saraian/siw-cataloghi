package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.Product;
import it.uniroma3.siw.model.Supplier;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long>{
	
	public boolean existsByName(String name);
	public List<Product> findByOrderByNameAsc();
	public List<Product> findByOrderByPriceAsc();
	public List<Product> findBySuppliersContaining(Supplier supplier);
}
