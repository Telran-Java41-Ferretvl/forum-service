package telran.java41.accounting.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import telran.java41.accounting.dto.exceptions.RoleNotFoundException;
import telran.java41.configuration.UserRoles;

@Getter
@EqualsAndHashCode(of = "login")
@Document(collection = "users")
public class UserAccount {
	@Id
	String login;
	@Setter
	String password;
	@Setter
	String firstName;
	@Setter
	String lastName;
	Set<UserRoles> roles;

	public UserAccount() {
		roles = new HashSet<>();
		roles.add(UserRoles.USER);
	}

	public UserAccount(String login, String password, String firstName, String lastName) {
		this();
		this.login = login;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public boolean addRole(String role) throws RoleNotFoundException  {
		for (UserRoles userRole : UserRoles.values()) {
			if (userRole.name().equalsIgnoreCase(role)) {
				return roles.add(userRole);
			}
		}
		throw new RoleNotFoundException(role);
	}

	public boolean removeRole(String role) throws RoleNotFoundException {
		for (UserRoles userRoles : UserRoles.values()) {
			if (userRoles.name().equalsIgnoreCase(role)) {
				return roles.remove(userRoles);
			}
		}
		throw new RoleNotFoundException(role);
	}

}
