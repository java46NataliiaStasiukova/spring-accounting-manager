package telran.spring.accounting.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.spring.accounting.entities.AccountEntity;

public interface AccountRepository extends MongoRepository<AccountEntity, String> {

	List<AccountEntity> findByExperationAfter(String string);

}
