package telran.spring.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import telran.spring.dto.Account;
import telran.spring.service.AccountingService;

@RestController
@RequestMapping("accounts")
@Validated //required annotation for additional validation of parameters
public class AccountingController {

	AccountingService accountingService;
	
	public AccountingController(AccountingService accountingService) {
		this.accountingService = accountingService;
	}
	
	@PostMapping
	String addAccount(@RequestBody @Valid Account account) {
		String res = String.format("account with username %s already exists", account.username);
		if (accountingService.addAccount(account)) {
			res = String.format("account with username %s has been updated", account.username);
		}
		return res;
	}
	@DeleteMapping("/{username}")
	String deleteAccount(@PathVariable @Email String username) {
		String res = String.format("account with username %s doesn't exist", username);
		if (accountingService.deleteAccount(username)) {
			res = String.format("account with username %s has been deleted", username);
		}
		return res;
	}
	@PutMapping
	String updateAccount(@RequestBody @Valid Account account) {
		String res = String.format("account with username %s doesn't exist", account.username);
		if (accountingService.updateAccount(account)) {
			res = String.format("account with username %s has been updated", account.username);
		}
		return res;
	}
	@GetMapping("/{username}")
	String hasAccount(@PathVariable @Email String username) {
		String res = String.format("account with username %s doesn't exist", username);
		if (accountingService.isExist(username)) {
			res = String.format("account with username %s exists", username);
		}
		return res;
	}
	@GetMapping
	List<String> getAccountsByRole(@RequestParam (name = "role",
	defaultValue = "")String role){
		return role.isEmpty() ? accountingService.getActiveAccounts() 
				: accountingService.getAccountsRole(role);
	}
	@GetMapping("roles/max")
	long getMaxRoles() {
		return accountingService.getMaxRoles();
	}

	

}

