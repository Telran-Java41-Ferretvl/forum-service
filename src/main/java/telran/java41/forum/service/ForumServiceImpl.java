package telran.java41.forum.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.java41.forum.dao.PostRepository;
import telran.java41.forum.dto.DatePeriodDto;
import telran.java41.forum.dto.NewCommentDto;
import telran.java41.forum.dto.NewPostDto;
import telran.java41.forum.dto.PostDto;
import telran.java41.forum.dto.exceptions.PostNotFoundException;
import telran.java41.model.Comment;
import telran.java41.model.Post;

@Service
public class ForumServiceImpl implements ForumService {
	
	PostRepository postRepository;
	ModelMapper modelMapper;

	@Autowired
	public ForumServiceImpl(PostRepository postRepository, ModelMapper modelMapper) {
		this.postRepository = postRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public PostDto addNewPost(NewPostDto newPost, String author) {
		Post post = modelMapper.map(newPost, Post.class);
		post.setAuthor(author);
		post = postRepository.save(post);
		return modelMapper.map(post, PostDto.class);
	}

	@Override
	public PostDto getPost(String id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
		return modelMapper.map(post, PostDto.class);
	}

	@Override
	public PostDto removePost(String id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
		postRepository.delete(post);
		return modelMapper.map(post, PostDto.class);
	}

	@Override
	public PostDto updatePost(NewPostDto postUpdateDto, String id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
		if (postUpdateDto.getContent() != null) {
			post.setContent(postUpdateDto.getContent());
		}
		if (postUpdateDto.getTitle() != null) {
			post.setTitle(postUpdateDto.getTitle());
		}
		if (postUpdateDto.getTags() != null) {
			post.getTags().addAll(postUpdateDto.getTags());
		}		
		postRepository.save(post);
		return modelMapper.map(post, PostDto.class);
	}

	@Override
	public void addLike(String id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
		post.addLike();
		postRepository.save(post);
	}

	@Override
	public PostDto addComment(String id, String author, NewCommentDto newCommentDto) {
		Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
		post.addComment(new Comment(author, newCommentDto.getMessage()));
		postRepository.save(post);
		return modelMapper.map(post, PostDto.class);
	}

	@Override
	public Iterable<PostDto> findPostsByAuthor(String author) {
		return postRepository.findByAuthorIgnoreCase(author)
				.map(p -> modelMapper.map(p, PostDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Iterable<PostDto> findPostsByTags(List<String> tags) {
		return postRepository.findByTagsInIgnoreCase(tags)
				.map(p -> modelMapper.map(p, PostDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Iterable<PostDto> findPostsByDates(DatePeriodDto datePeriodDto) {
		return postRepository.findBydateCreatedBetween(datePeriodDto.getDateFrom(), datePeriodDto.getDateTo())
				.map(p -> modelMapper.map(p, PostDto.class))
				.collect(Collectors.toList());
	}
}