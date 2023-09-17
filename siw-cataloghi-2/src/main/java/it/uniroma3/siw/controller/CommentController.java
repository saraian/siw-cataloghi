package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Comment;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Product;
import it.uniroma3.siw.repository.CommentRepository;
import it.uniroma3.siw.repository.ProductRepository;
import it.uniroma3.siw.service.CommentService;
import it.uniroma3.siw.service.CredentialsService;

@Controller
public class CommentController {
	
	@Autowired CredentialsService credentialsService;
	@Autowired CommentService commentService;
	@Autowired ProductRepository productRepository;
	@Autowired CommentRepository commentRepository;
	
	@GetMapping("/addComment/{idProduct}")
	public String addCommentPage(@PathVariable("idProduct") Long id, Model model) {
		Product product=this.productRepository.findById(id).get();
		UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials=credentialsService.getCredentials(userDetails.getUsername());
		model.addAttribute("product", product);
		model.addAttribute("newComment", new Comment());
		model.addAttribute("userComments", this.commentService.commentsByCredentials(credentials, product.getComments()));
		model.addAttribute("comments", this.commentService.commentsNotByCredentials(credentials, product.getComments()));
		return "productAddComment.html";
	}
	
	@PostMapping("/addingComment/{idProduct}")
	public String addComment(@ModelAttribute("newComment") Comment newComment, @PathVariable("idProduct") Long id, Model model) {
		Product product=this.productRepository.findById(id).get();
		UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials=credentialsService.getCredentials(userDetails.getUsername());
		newComment.setCredentials(credentials);
		product.getComments().add(newComment);
		this.commentRepository.save(newComment);
		this.productRepository.save(product);
		model.addAttribute("product", product);
		model.addAttribute("userComments", this.commentService.commentsByCredentials(credentials, product.getComments()));
		model.addAttribute("comments", this.commentService.commentsNotByCredentials(credentials, product.getComments()));
		return "product.html";
	}
	
	@GetMapping("/updateComment/{idProduct}/{idComment}")
	public String updateCommentPage(@PathVariable("idProduct") Long idP, @PathVariable("idComment") Long idC, Model model) {
		UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials=credentialsService.getCredentials(userDetails.getUsername());
		Product product=this.productRepository.findById(idP).get();
		Comment comment=this.commentRepository.findById(idC).get();
		model.addAttribute("upComment", comment);
		model.addAttribute("product", product);
		model.addAttribute("userComments", this.commentService.commentsByCredentials(credentials, product.getComments()));
		model.addAttribute("comments", this.commentService.commentsNotByCredentials(credentials, product.getComments()));
		return "productUpdateComment.html";
	}
	
	@PostMapping("/updateComment/{idProduct}/{idComment}")
	public String saveUpdate(@ModelAttribute("upComment") Comment comment, @PathVariable("idProduct") Long id,
			@PathVariable("idComment") Long idc, Model model) {
		Product product=this.productRepository.findById(id).get();
		UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials=credentialsService.getCredentials(userDetails.getUsername());
		if(!comment.getTitle().isBlank()&&!comment.getText().isBlank()) {
			Comment found=this.commentRepository.findById(idc).get();
			found.setTitle(comment.getTitle());
			found.setText(comment.getText());
			this.commentRepository.save(found);
			model.addAttribute("product", product);
			model.addAttribute("userComments", this.commentService.commentsByCredentials(credentials, product.getComments()));
			model.addAttribute("comments", this.commentService.commentsNotByCredentials(credentials, product.getComments()));
			return "product.html";
		}
		model.addAttribute("messaggioErrore", "Compila entrambi i campi del commento");
		model.addAttribute("product", product);
		model.addAttribute("userComments", this.commentService.commentsByCredentials(credentials, product.getComments()));
		model.addAttribute("comments", this.commentService.commentsNotByCredentials(credentials, product.getComments()));
		return "productUpdateComment.html";
	}
}