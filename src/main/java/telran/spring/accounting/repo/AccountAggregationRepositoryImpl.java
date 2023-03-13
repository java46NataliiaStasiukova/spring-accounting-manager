package telran.spring.accounting.repo;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import org.springframework.stereotype.Repository;

import telran.spring.accounting.entities.AccountEntity;
import telran.spring.accounting.service.AccountingService;
import static com.mongodb.client.model.Sorts.ascending;

@Repository
public class AccountAggregationRepositoryImpl implements AccountAggregationRepository {

	private static Logger LOG = LoggerFactory.getLogger(AccountingService.class);
	@Autowired
	MongoTemplate mongoTemplate;
	@Override
	public long getMaxRoles() {
		ArrayList<AggregationOperation> operations = new ArrayList<>();
		operations.add(unwind("roles"));
		operations.add(group("email").count().as("count"));
		operations.add(group().max("count").as("maxCount"));
		Aggregation pipeline = newAggregation(operations);
		var document = mongoTemplate.aggregate(pipeline,
				AccountEntity.class, Document.class);
		return document.getUniqueMappedResult().getInteger("maxCount");
	}

	@Override
	public int getAccountsCount() {
		ArrayList<AggregationOperation> operations = new ArrayList<>();
		operations.add(unwind("roles"));
		operations.add(group("email").count().as("count"));
		operations.add(group("roles").max("count").as("maxCount"));
		//operations.add(sort(Sort.Direction.DESC, "count"));
		//operations.add();
		Aggregation pipeline = newAggregation(operations);
		var document = mongoTemplate.aggregate(pipeline,
				AccountEntity.class, Document.class);
		LOG.info("document {}", document.getMappedResults());
		LOG.info("document {}", document.getMappedResults().stream().map(e -> e.toJson()).toList());
		return 0;
	}

}
