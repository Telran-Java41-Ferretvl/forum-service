package telran.java41.security.service;

import telran.java41.accounting.model.UserAccount;

public interface SessionService {

	UserAccount addUser(String sessionID, UserAccount userAccount);
	
	UserAccount getUser(String sessionID);
	
	UserAccount removeUser(String sessionID);
}
