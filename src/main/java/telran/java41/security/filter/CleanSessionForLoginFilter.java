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

import telran.java41.security.service.SessionService;

@Service
@Order(5)
public class CleanSessionForLoginFilter implements Filter {

	SessionService sessionService;

	@Autowired
	public CleanSessionForLoginFilter(SessionService sessionService) {
		this.sessionService = sessionService;
	}


	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		if (checkEndpoint(request.getMethod(), request.getServletPath())) {
			String sessionID = request.getSession().getId();
			sessionService.removeUser(sessionID);
		}
		
		chain.doFilter(request, response);
	}
	
	private boolean checkEndpoint(String method, String path) {
		return path.matches("/account/login/?");
	}

}
