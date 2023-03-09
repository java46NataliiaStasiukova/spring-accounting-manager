package telran.spring.accounting.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import telran.spring.accounting.model.Account;

@Document(collection = "accounts") 
public class AccountEntity {
	@Id
	private String email;
	private String password;
	private LocalDateTime expiration;
	private boolean revoked;
	private String[] roles;
	public String[] getRoles() {
		return roles;
	}
	
	public void setRoles(String[] roles) {
		this.roles = roles;
	} 
	
	public static AccountEntity of(Account accountDto) {
		AccountEntity account = new AccountEntity();
		account.password = accountDto.password;
		account.email = accountDto.username;
		account.revoked = false;
		account.roles = accountDto.roles;
		return account;
	} 
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public LocalDateTime getExpiration() {
		return expiration;
	}
	
	public void setExpiration(LocalDateTime expiration) {
		this.expiration = expiration;
	}
	
	public boolean isRevoked() {
		return revoked;
	}
	
	public void setRevoked(boolean revoked) {
		this.revoked = revoked;
	}
	
	public String getEmail() {
		return email;
	}
	
	
	
	
}
