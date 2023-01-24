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
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import telran.spring.controller.AccountingController;
import telran.spring.dto.Account;

@Service
public class AccountingServiceImpl implements AccountingService {
	private static Logger LOG = LoggerFactory.getLogger(AccountingService.class);
	@Value("${app.admin.username:admin}")
	private String admin;
private PasswordEncoder passwordEncoder;
private UserDetailsManager userDetailsManager;
private HashMap<String, Account> accounts;
@Value("${app.file.name:accounts.data}")
private String fileName;

	@Override
	public boolean addAccount(Account account) {
		boolean res = false;
		if(!account.username.equals(admin) && !accounts.containsKey(account.username)) {
			res = true;
			account.password = passwordEncoder.encode(account.password);
			accounts.put(account.username, account);
			userDetailsManager.createUser(User.withUsername(account.username)
					.password(account.password).roles(account.role).build());
		}
		return res;
	}

	@Override
	public boolean deleteAccount(String username) {
		boolean res = false;
		if(accounts.containsKey(username)) {
			res = true;
			accounts.remove(username);
			userDetailsManager.deleteUser(username);
		}
		return res;
	}

	@Override
	public boolean updateAccount(Account account) {
		boolean res = false;
		if(accounts.containsKey(account.username)) {
			res = true;
			account.password = passwordEncoder.encode(account.password);
			accounts.put(account.username, account);
			userDetailsManager.updateUser(User.withUsername(account.username)
					.password(account.password).roles(account.role).build());
		}
		return res;
	}

	@Override
	public boolean isExist(String username) {
		return accounts.containsKey(username);
	}

	public AccountingServiceImpl(PasswordEncoder passwordEncoder, UserDetailsManager userDetailsManager) {
		this.passwordEncoder = passwordEncoder;
		this.userDetailsManager = userDetailsManager;
	}
	@PreDestroy
	void saveAccounts() {
		try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileName))){
			output.writeObject(accounts);
			LOG.debug("accounts saved to file {}", fileName);
		} catch(Exception e) {
			LOG.error("saving to file caused exception {}", e.getMessage());
		}
	}
	@PostConstruct
	void restoreAccounts() {
		try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileName))){
			accounts = (HashMap<String, Account>) input.readObject();
			for(Account acc: accounts.values()) {
				userDetailsManager.createUser(User.withUsername(acc.username)
						.password(acc.password).roles(acc.role).build());
			}
			LOG.debug("accounts {} has been restored", accounts.keySet());
		}catch(FileNotFoundException e) {
			LOG.warn("file {} doesn't exists", fileName);
			accounts = new HashMap<>();
		}catch (Exception e) {
			LOG.error("error at restoring accounts {}", e.getMessage());
		}
	}
}
//@Service
//public class AccountingServiceImpl implements AccountingService {
//HashMap<String, Account> accounts;
//InMemoryUserDetailsManager manager;
//PasswordEncoder encoder;
//@Value("${app.filename.file:newFile}")
//String fileName;
//@Value("${app.username.admin:admin}")
//String admin;
//static Logger LOG = LoggerFactory.getLogger(AccountingController.class);
//
//public AccountingServiceImpl(HashMap<String, Account> accounts, InMemoryUserDetailsManager manager,
//		PasswordEncoder encoder){
//	this.accounts = accounts;
//	this.manager = manager;
//	this.encoder = encoder;
//}
//	@SuppressWarnings("unchecked")
//	public void init() throws Exception{
//		if(new File(fileName).exists()) {
//			try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(Path.of(fileName)))) {
//				accounts = (HashMap<String, Account>) input.readObject();
//				accounts.entrySet().stream()
//				.forEach(k -> {
//					manager.createUser(User.withUsername(k.getValue().username)
//							.password(encoder.encode(k.getValue().password)).roles(k.getValue().role).build());
//				});
//			}
//			LOG.debug("accounts was restored from file: {}", fileName);
//		} 
//	}
//	
//	public void save() throws Exception{
//		try(ObjectOutputStream output = new ObjectOutputStream(
//				new FileOutputStream(fileName))){
//			output.writeObject(accounts);
//		}
//		LOG.debug("accounts was saved to file: {}", fileName);
//	}
//	
//	@Override
//	public Boolean addAccount(Account account) {
//		//if(!account.username.equals(admin)) {
//			account.password = encoder.encode(account.password);
//			if(accounts.putIfAbsent(account.username, account) == null) {
//				manager.createUser(User.withUsername(account.username)
//						.password(account.password).roles(account.role).build());
//				LOG.debug("account was created with user name: {}, password: {}, role: {}", 
//						account.username, account.password, account.role);
//				return true;
//			}
//		//}
//		LOG.debug("account was NOT created with user name: {}, password: {}, role: {}", 
//				account.username, account.password, account.role);
//		return false;
//	}
//
//	@Override
//	public Boolean deleteAccount(String userName) {
//		if(accounts.remove(userName) != null) {
//			manager.deleteUser(userName);
//			LOG.debug("user with user name: {} was deleted", userName);
//			return true;
//		}
//		LOG.debug("user with user name: {} was NOT deleted", userName);
//		return false;
//	}
//
//	@Override
//	public Boolean updateAccount(Account account) {		
//		if(accounts.containsKey(account.username)) {
//			account.password = encoder.encode(account.password);
//			accounts.put(account.username, account);
//			manager.updateUser(User.withUsername(account.username)
//					.password(account.password).roles(account.role).build());
//			LOG.debug("account was updated with user name: {}, password: {}, role: {}", 
//					account.username, account.password, account.role);
//			return true;
//		}
//		LOG.debug("account was NOT updated with user name: {}, password: {}, role: {}", 
//				account.username, account.password, account.role);
//		return false;
//	}
//	
//	@Override
//	public Boolean isExist(String userName) {
//		LOG.debug("user with user name: {} exist:{}", userName, userName.equals(admin) ? true : accounts.containsKey(userName));
//		return accounts.containsKey(userName);
//	}
//
//}
