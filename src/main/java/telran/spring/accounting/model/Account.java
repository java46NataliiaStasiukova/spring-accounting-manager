package telran.spring.accounting.model;



import jakarta.validation.constraints.*;

public class Account{
	
	@NotEmpty @Email
	public String username;
	 @Size(min = 6, message = "password must have length not less than 6") @NotEmpty
	public String password;
	@NotEmpty(message = "should be at least one role")
	public String[] roles;
}
