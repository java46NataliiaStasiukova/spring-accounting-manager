package telran.spring.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import telran.spring.accounting.entities.AccountEntity;
import telran.spring.accounting.repo.AccountRepository;
import telran.spring.dto.Account;

@Service
@Transactional
public class AccountingServiceImpl implements AccountingService {
	private static Logger LOG = LoggerFactory.getLogger(AccountingService.class);
	@Value("${app.admin.username:admin}")
	private String admin;
	@Value("${app.password.period:100}")
	private int passwordPeriod;//period password existence in hours
private PasswordEncoder passwordEncoder;
private UserDetailsManager userDetailsManager;
private AccountRepository accounts;
@Value("${app.file.name:accounts.data}")
private String fileName;

public AccountingServiceImpl(PasswordEncoder passwordEncoder, UserDetailsManager userDetailsManager,
		AccountRepository accounts) {
	this.passwordEncoder = passwordEncoder;
	this.userDetailsManager = userDetailsManager;
	this.accounts = accounts;
}

	@Override
	public boolean addAccount(Account account) {
		boolean res = false;
		if(!account.username.equals(admin) && !accounts.existsById(account.username)) {
			res = true;
			account.password = passwordEncoder.encode(account.password);
			AccountEntity accountDocument = AccountEntity.of(account);
			accountDocument.setExperation(LocalDateTime.now().plusHours(passwordPeriod));
			accounts.save(accountDocument);
			userDetailsManager.createUser(User.withUsername(account.username)
					.password(account.password).roles(account.roles).build());
		}
		return res;
	}

	@Override
	public boolean deleteAccount(String username) {
		boolean res = false;
		if(accounts.existsById(username)) {
			res = true;
			accounts.deleteById(username);
			userDetailsManager.deleteUser(username);
		}
		return res;
	}

	@Override
	public boolean updateAccount(Account account) {
		boolean res = false;
		AccountEntity accountDocument = accounts.findById(account.username).orElse(null);
		if(accountDocument != null) {
			//if(!passwordEncoder.matches(account.password, accountDocument.getPassword())) {
				res = true;
				account.password = passwordEncoder.encode(account.password);
				accountDocument.setPassword(account.password);
				accountDocument.setRevoke(false);
				accountDocument.setRoles(account.roles);
				accountDocument.setExperation(LocalDateTime.now().plusHours(passwordPeriod));
				accounts.save(accountDocument);
				userDetailsManager.updateUser(User.withUsername(account.username)
						.password(account.password).roles(account.roles).build());
			//}
		}
		return res;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isExist(String username) {
		return userDetailsManager.userExists(username);
	}

	@PostConstruct
	@Transactional(readOnly = true)
	void detailsManagerPopulation() {
		//TO FIX
		List<AccountEntity> notExpiredAccounts = accounts.findByExperationAfter(LocalDateTime.now().toString());
		for(AccountEntity acc: notExpiredAccounts) {
			if(!acc.isRevoke()) {
				userDetailsManager.createUser(User.withUsername(acc.getEmail())
						.password(acc.getPassword()).roles(acc.getRoles()).build());
			}
		}
		LOG.debug("accounts {} has been restored", notExpiredAccounts.size());
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> getAccountsRole(String role) {
		List<AccountEntity> accountsDB = accounts.findByRole(role);
		LOG.debug("passwords: {}", accountsDB.stream().map(AccountEntity::getPassword).toList());
		return accountsDB.stream().map(AccountEntity::getEmail).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> getActiveAccounts() {
		List<AccountEntity> accountsDB = accounts.findByExpirationGreaterThanAndRevokedIsFalse(LocalDateTime.now(ZoneId.of("UTC")));
		return accountsDB.stream().map(AccountEntity::getEmail).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public long getMaxRoles() {
		
		return accounts.getMaxRoles();
	}
}

