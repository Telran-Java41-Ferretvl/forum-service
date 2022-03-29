package telran.java41.security.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import telran.java41.accounting.model.UserAccount;

@Service
public class SessionServiceImpl implements SessionService {
	
	Map<String, UserAccount> users = new ConcurrentHashMap<>();

	@Override
	public UserAccount addUser(String sessionID, UserAccount userAccount) {
		return users.put(sessionID, userAccount);
	}

	@Override
	public UserAccount getUser(String sessionID) {
		return users.get(sessionID);
	}

	@Override
	public UserAccount removeUser(String sessionID) {
		return users.remove(sessionID);
	}

}
