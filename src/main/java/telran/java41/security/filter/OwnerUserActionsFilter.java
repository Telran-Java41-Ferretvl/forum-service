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

@Service
@Order(15)
public class OwnerUserActionsFilter implements Filter {

	UserAccountRepository repository;

	@Autowired
	public OwnerUserActionsFilter(UserAccountRepository repository) {
		this.repository = repository;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		Principal principal = request.getUserPrincipal();

		if (!(principal == null) && checkEndpoint(request.getMethod(), request.getServletPath())) {

			String temp[] = request.getRequestURI().split("/");
			String login = temp[temp.length - 1];

			if (!(principal.getName().equals(login))) {
				response.sendError(403);
				return;
			}
		}

		chain.doFilter(request, response);
	}

	private boolean checkEndpoint(String method, String path) {
		return path.matches("/account/user/\\w+/?");
	}

}