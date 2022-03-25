package telran.java41.accounting.service;

import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.java41.accounting.dao.UserAccountRepository;
import telran.java41.accounting.dto.RolesResponseDto;
import telran.java41.accounting.dto.UserAccountResponseDto;
import telran.java41.accounting.dto.UserRegisterDto;
import telran.java41.accounting.dto.UserUpdateDto;
import telran.java41.accounting.dto.exceptions.UserExistsException;
import telran.java41.accounting.dto.exceptions.UserNotFoundException;
import telran.java41.accounting.model.UserAccount;

@Service
public class UserAccountServiceImpl implements UserAccountService {

	UserAccountRepository repository;
	ModelMapper modelMapper;
	
	@Autowired
	public UserAccountServiceImpl(UserAccountRepository repository, ModelMapper modelMapper) {
		this.repository = repository;
		this.modelMapper = modelMapper;
	}

	@Override
	public UserAccountResponseDto addUser(UserRegisterDto userRegisterDto) {
		if (repository.existsById(userRegisterDto.getLogin())) {
			throw new UserExistsException(userRegisterDto.getLogin());
		}
		UserAccount userAccount = modelMapper.map(userRegisterDto, UserAccount.class);
		String password = BCrypt.hashpw(userRegisterDto.getPassword(), BCrypt.gensalt());
		userAccount.setPassword(password);
		repository.save(userAccount);
		return modelMapper.map(userAccount, UserAccountResponseDto.class);
	}

	@Override
	public UserAccountResponseDto getUser(String login) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException());
		return modelMapper.map(userAccount, UserAccountResponseDto.class);
	}

	@Override
	public UserAccountResponseDto removeUser(String login) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException());
		repository.delete(userAccount);
		return modelMapper.map(userAccount, UserAccountResponseDto.class);
	}

	@Override
	public UserAccountResponseDto editUser(String login, UserUpdateDto userUpdateDto) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException());
		if (userUpdateDto.getFirstName() != null) {
			userAccount.setFirstName(userUpdateDto.getFirstName());
		}
		if (userUpdateDto.getLastName() != null) {
			userAccount.setLastName(userUpdateDto.getLastName());
		}
		repository.save(userAccount);
		return modelMapper.map(userAccount, UserAccountResponseDto.class);
	}

	@Override
	public RolesResponseDto changeRolesList(String login, String role, boolean isAddRole) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException());
		if (isAddRole) {
			userAccount.addRole(role);
		}
		else {
			userAccount.removeRole(role);
		}
		repository.save(userAccount);
		return modelMapper.map(userAccount, RolesResponseDto.class);
	}

	@Override
	public void changePassword(String login, String newPassword) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException());
		userAccount.setPassword(newPassword);
		repository.save(userAccount);
	}

}
