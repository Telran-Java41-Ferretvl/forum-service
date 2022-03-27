package telran.java41.security.filter;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import telran.java41.accounting.dao.UserAccountRepository;
import telran.java41.accounting.model.UserAccount;
import telran.java41.configuration.UserRoles;
import telran.java41.forum.dao.PostRepository;
import telran.java41.model.Post;

@Service
@Order(13)
public class OwnerPostActionsFilter implements Filter {

	UserAccountRepository userRepository;
	PostRepository postRepository;

	@Autowired
	public OwnerPostActionsFilter(UserAccountRepository userRepository, PostRepository postRepository) {
		this.userRepository = userRepository;
		this.postRepository = postRepository;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		Principal principal = request.getUserPrincipal();

		if (!(principal == null) && checkEndpoint(request.getMethod(), request.getServletPath())) {

			UserAccount userAccount = userRepository.findById(principal.getName()).orElse(null);
			String[] temp = request.getRequestURI().split("/");
			String login = temp[temp.length - 1];
			Post currentPost = postRepository.findById(login).orElse(null);

			if (currentPost == null) {
				response.sendError(404, "Post not found");
				return;
			}
			if (request.getMethod().equals("DELETE") && !userAccount.getRoles().contains(UserRoles.MODERATOR)
					&& !(userAccount.getLogin().equals(currentPost.getAuthor()))) {
				response.sendError(403);
				return;
			}
			if (request.getMethod().equals("PUT") && !(userAccount.getLogin().equals(currentPost.getAuthor()))) {
				response.sendError(403);
				return;
			}

		}
		chain.doFilter(request, response);
	}

	private boolean checkEndpoint(String method, String path) {
		return (path.matches("/forum/post/\\w+/?")
				&& ("DELETE".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method)));

	}
}
