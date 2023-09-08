package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.Comment;
import it.uniroma3.siw.model.Credentials;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long>{
	
}
