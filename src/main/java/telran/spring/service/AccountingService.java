package telran.spring.service;

import telran.spring.dto.Account;

public interface AccountingService{

	boolean addAccount(Account account);
	
	boolean deleteAccount(String userName);
	
	boolean updateAccount(Account account);
	
	boolean isExist(String userName);
}
