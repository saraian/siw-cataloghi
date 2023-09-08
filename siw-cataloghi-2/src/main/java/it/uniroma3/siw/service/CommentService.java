package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Comment;
import it.uniroma3.siw.model.Credentials;

@Service
public class CommentService {

	public List<Comment> commentsByCredentials(Credentials credentials, List<Comment> comments) {
		List<Comment> userComments=new ArrayList<Comment>();
		Iterator<Comment> iterator=comments.iterator();
		while(iterator.hasNext()) {
			Comment comment=iterator.next();
			if(comment.getCredentials().equals(credentials))
				userComments.add(comment);
		}
		return userComments;
	}
	
	public List<Comment> commentsNotByCredentials(Credentials credentials, List<Comment> comments) {
		List<Comment> otherComments=new ArrayList<Comment>();
		Iterator<Comment> iterator=comments.iterator();
		while(iterator.hasNext()) {
			Comment comment=iterator.next();
			if(!comment.getCredentials().equals(credentials))
				otherComments.add(comment);
		}
		return otherComments;
	}
}
