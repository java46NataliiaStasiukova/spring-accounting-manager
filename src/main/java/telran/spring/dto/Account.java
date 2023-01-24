package telran.spring.dto;

import java.io.Serializable;

import jakarta.validation.constraints.*;

public class Account  implements Serializable{
private static final long serialVersionUID = 1L;

@Email @NotEmpty
public String username;
@Size(min = 6, message = "password must have length not les than 6") @NotEmpty
public String password;
@NotEmpty
public String role;

}