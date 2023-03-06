package telran.spring.accounting.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import telran.spring.accounting.entities.AccountEntity;
//import telran.spring.accounting.projection.AccountName;

public interface AccountRepository extends MongoRepository<AccountEntity, String>, 
	AccountAggregationRepository{

	List<AccountEntity> findByExperationAfter(String string);

	@Query(value="{roles:{$elemMatch:{$eq:?0}}}", fields = "{email: 1}")
	//List<AccountName> findByRole(String role);
	List<AccountEntity> findByRole(String role);


	List<AccountEntity> findByExpirationGreaterThanAndRevokedIsFalse(LocalDateTime ldt);

}
