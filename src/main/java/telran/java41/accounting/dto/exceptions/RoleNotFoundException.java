package telran.java41.accounting.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class RoleNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 6066364373531884549L;

	public RoleNotFoundException(String role) {
		super("Role " + role + " not found");
	}
}
