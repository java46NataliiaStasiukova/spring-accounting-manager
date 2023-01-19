package telran.spring.service;

import telran.spring.dto.Account;

public interface AccountingService{

	Boolean addAccount(Account account);
	
	Boolean deleteAccount(String userName);
	
	Boolean updateAccount(Account account);
	
	Boolean isExist(String userName);
}
