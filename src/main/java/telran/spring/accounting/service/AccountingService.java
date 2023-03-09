package telran.spring.accounting.service;

import java.util.List;

import telran.spring.accounting.model.Account;

public interface AccountingService {
boolean addAccount(Account account) ;
boolean deleteAccount(String username);
boolean updateAccount(Account account);
boolean isExists(String username);
List<String> getAccountsRole(String role) ;
List<String> getActiveAccounts();
long getMaxRoles();
List<String> getAllAccountsWithMaxRoles(); //email's of accounts with maximal roles amount
int getMaxRolesOccurrenceCount(); //example, role1 - in 10 accounts, role100 - 5; role2 - 5, role3 - 20; role4 - 20; max = 20
List<String> getAllRolesWithMaxOccurrrence(); //role3, role4 from the above example
int getActiveMinRolesOccurrenceCount(); //similar to getMaxRolesOccurrenceCount but minimal value and only among the active accounts (neither expired nor revoked)
}
