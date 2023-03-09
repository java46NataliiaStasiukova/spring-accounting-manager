package telran.spring.accounting.repo;

import java.util.ArrayList;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import org.springframework.stereotype.Repository;

import telran.spring.accounting.entities.AccountEntity;

@Repository
public class AccountAggregationRepositoryImpl implements AccountAggregationRepository {
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

}
