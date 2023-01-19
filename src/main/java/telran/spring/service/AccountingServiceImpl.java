package telran.spring.service;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import telran.spring.controller.AccountingController;
import telran.spring.dto.Account;

@Service
public class AccountingServiceImpl implements AccountingService {
HashMap<String, Account> accounts;
InMemoryUserDetailsManager manager;
PasswordEncoder encoder;
@Value("${app.filename.file:newFile}")
String fileName;
@Value("${app.username.admin:admin}")
String admin;
static Logger LOG = LoggerFactory.getLogger(AccountingController.class);

public AccountingServiceImpl(HashMap<String, Account> accounts, InMemoryUserDetailsManager manager,
		PasswordEncoder encoder){
	this.accounts = accounts;
	this.manager = manager;
	this.encoder = encoder;
}
	@SuppressWarnings("unchecked")
	public void init() throws Exception{
		if(new File(fileName).exists()) {
			try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(Path.of(fileName)))) {
				accounts = (HashMap<String, Account>) input.readObject();
				accounts.entrySet().stream()
				.forEach(k -> {
					manager.createUser(User.withUsername(k.getValue().username)
							.password(encoder.encode(k.getValue().password)).roles(k.getValue().role).build());
				});
			}
			LOG.debug("accounts was restored from file: {}", fileName);
		} 
	}
	
	public void save() throws Exception{
		try(ObjectOutputStream output = new ObjectOutputStream(
				new FileOutputStream(fileName))){
			output.writeObject(accounts);
		}
		LOG.debug("accounts was saved to file: {}", fileName);
	}
	
	@Override
	public Boolean addAccount(Account account) {
		//if(!account.username.equals(admin)) {
			account.password = encoder.encode(account.password);
			if(accounts.putIfAbsent(account.username, account) == null) {
				manager.createUser(User.withUsername(account.username)
						.password(account.password).roles(account.role).build());
				LOG.debug("account was created with user name: {}, password: {}, role: {}", 
						account.username, account.password, account.role);
				return true;
			}
		//}
		LOG.debug("account was NOT created with user name: {}, password: {}, role: {}", 
				account.username, account.password, account.role);
		return false;
	}

	@Override
	public Boolean deleteAccount(String userName) {
		if(accounts.remove(userName) != null) {
			manager.deleteUser(userName);
			LOG.debug("user with user name: {} was deleted", userName);
			return true;
		}
		LOG.debug("user with user name: {} was NOT deleted", userName);
		return false;
	}

	@Override
	public Boolean updateAccount(Account account) {		
		if(accounts.containsKey(account.username)) {
			account.password = encoder.encode(account.password);
			accounts.put(account.username, account);
			manager.updateUser(User.withUsername(account.username)
					.password(account.password).roles(account.role).build());
			LOG.debug("account was updated with user name: {}, password: {}, role: {}", 
					account.username, account.password, account.role);
			return true;
		}
		LOG.debug("account was NOT updated with user name: {}, password: {}, role: {}", 
				account.username, account.password, account.role);
		return false;
	}
	
	@Override
	public Boolean isExist(String userName) {
		LOG.debug("user with user name: {} exist:{}", userName, userName.equals(admin) ? true : accounts.containsKey(userName));
		return accounts.containsKey(userName);
	}

}
