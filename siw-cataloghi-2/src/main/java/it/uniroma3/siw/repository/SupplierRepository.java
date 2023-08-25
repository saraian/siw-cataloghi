package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.Supplier;

@Repository
public interface SupplierRepository extends CrudRepository<Supplier, Long>{
	
	public boolean existsByName(String name);
}
