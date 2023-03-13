package telran.spring.accounting.repo;

import java.util.List;

import telran.spring.accounting.entities.AccountEntity;

public interface AccountAggregationRepository {
	
long getMaxRoles();

int getAccountsCount();
}
