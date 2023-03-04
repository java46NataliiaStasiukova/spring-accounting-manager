package telran.spring.accounting.entities;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import telran.spring.dto.Account;

@Document(collection = "accounts")
public class AccountEntity {

	@Id
	private String id;
	private String username;
	String password;
	LocalDateTime experation;
	boolean revoke;
	private String[] roles;
	
	public static AccountEntity of(Account accountDto) {
		AccountEntity account = new AccountEntity();
		account.password = accountDto.password;
		account.username = accountDto.username;
		account.revoke = false;
		account.roles = accountDto.roles;
		account.id = accountDto.username;
		return account;
	}
	
	public String getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDateTime getExperation() {
		return experation;
	}

	public void setExperation(LocalDateTime experation) {
		this.experation = experation;
	}

	public boolean isRevoke() {
		return revoke;
	}

	public void setRevoke(boolean revoke) {
		this.revoke = revoke;
	}

	public String getEmail() {
		return username;
	}
	
	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "AccountEntity [email=" + username + ", password=" + password + ", experation=" + experation + ", revoke="
				+ revoke + ", roles=" + Arrays.toString(roles) + "]";
	}
}
