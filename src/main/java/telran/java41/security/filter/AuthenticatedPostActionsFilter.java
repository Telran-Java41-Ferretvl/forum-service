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

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import telran.java41.accounting.dao.UserAccountRepository;
import telran.java41.accounting.model.UserAccount;
import telran.java41.forum.dao.PostRepository;

@Service
@Order(14)
public class AuthenticatedPostActionsFilter implements Filter {
	
	PostRepository postRepository;
	UserAccountRepository userRepository;


	public AuthenticatedPostActionsFilter(PostRepository postRepository, UserAccountRepository userRepository) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		Principal principal = request.getUserPrincipal();
		if (!(principal==null) && checkEndpoint(request.getMethod(), request.getServletPath())) {
			UserAccount userAccount = userRepository.findById(principal.getName()).orElse(null);
			String [] temp = request.getRequestURI().split("/");
			String login = temp[temp.length - 1];
			if (!(userAccount.getLogin().equals(login))) {
				response.sendError(403);
				return;
			}
		}
		chain.doFilter(request, response);
		
	}
	
	private boolean checkEndpoint(String method, String path) {
		return ((path.matches("/forum/post/\\w+/?") 
				&& (HttpMethod.POST.toString().equalsIgnoreCase(method)))
				|| (path.matches("/forum/post/\\w+/comment/\\w+/?")
						&& HttpMethod.PUT.toString().equalsIgnoreCase(method)));
				 
	}
}
