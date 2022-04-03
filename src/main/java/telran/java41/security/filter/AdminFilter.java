package telran.java41.security.filter;

import java.io.IOException;

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

import lombok.AllArgsConstructor;
import telran.java41.accounting.dao.UserAccountRepository;
import telran.java41.accounting.model.UserAccount;
import telran.java41.configuration.UserRoles;
import telran.java41.security.context.SecurityContext;
import telran.java41.security.context.User;

@Service
@Order(20)
@AllArgsConstructor
public class AdminFilter implements Filter {
	
	SecurityContext context;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		if (checkEndpoint(request.getMethod(), request.getServletPath())) {
			User user = context.getUser(request.getUserPrincipal().getName());
			if (!user.getRoles().contains(UserRoles.ADMINISTRATOR)) {
				response.sendError(403);
				return;
			}
		}

		chain.doFilter(request, response);
	}

	private boolean checkEndpoint(String method, String path) {
		return path.matches("/account/user/\\w+/role/\\w+/?");
	}

}
