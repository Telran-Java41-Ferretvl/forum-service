package telran.java41.forum.dao;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.java41.model.Post;

public interface PostRepository extends MongoRepository<Post, String> {

	Stream<Post> findByAuthorIgnoreCase(String name);

	Stream<Post> findByTagsInIgnoreCase(Iterable<String> tags);
	
	Stream<Post> findBydateCreatedBetween(LocalDate from, LocalDate to);
}
