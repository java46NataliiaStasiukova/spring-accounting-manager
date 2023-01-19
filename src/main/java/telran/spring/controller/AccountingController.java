package telran.spring.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PreDestroy;
import jakarta.validation.Valid;
import telran.spring.dto.Account;
import telran.spring.service.AccountingServiceImpl;

@RestController
@RequestMapping("accounts")
public class AccountingController {
	static Logger LOG = LoggerFactory.getLogger(AccountingController.class);
	@Value("${app.username.admin:admin}")
	String admin;
	AccountingServiceImpl accounts;
	PasswordEncoder encoder;
	UserDetailsManager manager;
	
	public AccountingController(PasswordEncoder encoder, UserDetailsManager manager,
			AccountingServiceImpl accounts) {
		this.encoder = encoder;
		this.manager = manager;
		this.accounts = accounts;
		try {
			accounts.init();
		} catch (Exception e) {
			LOG.error("error in restoring accounts from file");
			e.printStackTrace();
		}
	}
	
	@PostMapping
	String addUser(@RequestBody @Valid Account account) {
		LOG.debug("request for adding account with user name: {}, "
				+ "password: {}, role: {}", account.username, account.password, account.role);
		String res = String.format("User with name %s already exists", account.username);
		if(accounts.addAccount(account)) {
			res = String.format("user %s has been added", account.username);
		}
		return res;
		
	}
	
	@PutMapping
	String updateUser(@RequestBody @Valid Account account) {
		LOG.debug("request for update account with user name: {}, "
				+ "password: {}, role: {}", account.username, account.password, account.role);
		String res = String.format("User with name %s doesn't exist", account.username);
		if(account.username.equals(admin)) {
			res = String.format("user %s can not be updated", account.username);
		}else if(accounts.updateAccount(account)) {
			res = String.format("user %s has been updated", account.username);
		}
		return res;
	}
	
	@DeleteMapping("/{username}")
	String deleteUser(@PathVariable("username") String username) {
		LOG.debug("request for delete account with user name: {}", username);
		String res = String.format("User with name %s doesn't exist", username);
		if(username.equals(admin)) {
			res = String.format("user %s can not be deleted", username);
		}else if(accounts.deleteAccount(username)) {
			res = String.format("user %s has been deleted", username);
		}
		return res;
	}
	
	@GetMapping("/{username}")
	boolean userExists(@PathVariable("username") String username) {
		LOG.debug("request for checking if user with user name: {}, exists: {}",
				username, manager.userExists(username));
		return accounts.isExist(username);
	}
	
	@PreDestroy
	void shutdown() {
		try {
			accounts.save();
		} catch (Exception e) {
			LOG.error("error in writing accounts into a file");
			e.printStackTrace();
		}
		System.out.println("Bye performed graceful shutdown");
		LOG.info("shutdown performed");
	}
	
	
	
}
