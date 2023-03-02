package telran.spring.dto;

import jakarta.validation.constraints.*;

public class Account{

@Email @NotEmpty
public String username;
@Size(min = 6, message = "password must have length not les than 6") @NotEmpty
public String password;
@NotEmpty(message = "should be at least one role")
public String[] roles;

}