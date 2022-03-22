package telran.java41.accounting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRegisterDto {
	String login;
	String password;
	String firstName;
	String lastName;
}
