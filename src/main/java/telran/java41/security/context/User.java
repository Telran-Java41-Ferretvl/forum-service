package telran.java41.security.context;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import telran.java41.configuration.UserRoles;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class User {
	String userName;
	String password;
	@Singular
	Set<UserRoles> roles;
}
