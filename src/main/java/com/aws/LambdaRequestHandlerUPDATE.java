package com.aws;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class LambdaRequestHandlerUPDATE implements RequestHandler<Person, Person> {
    @Override
    public Person handleRequest(Person person, Context context) {
        Table table = this.initDynamoDbClient();

//        Map<String, String> experssionAttributesNames = new HashMap<>();
//        experssionAttributesNames.put("#name", "name");
//        experssionAttributesNames.put("#address", "address");
//        experssionAttributesNames.put("#age", "age");
//
//        Map<String, Object> expressionAttributesValues = new HashMap<>();
//        expressionAttributesValues.put(":name", person.getName());
//        expressionAttributesValues.put(":address", person.getAddress());
//        expressionAttributesValues.put(":age", person.getAge());


        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("Id", person.getId())
                .withUpdateExpression("set #name =:name, #address =:address,#age=:age")
                .withNameMap(new NameMap().with("#name", "name").with("#address", "address").with("#age", "age"))
                .withValueMap(new ValueMap().withString(":name", person.getName())
                        .withString(":address", person.getAddress())
                        .withNumber(":age", person.getAge()))
                .withReturnValues(ReturnValue.UPDATED_NEW);

        Person response = null;
        try {
            UpdateItemOutcome updateItemOutcome = table.updateItem(updateItemSpec);
            response = new ObjectMapper().readValue(updateItemOutcome.getItem().toJSONPretty(), Person.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return response;
    }

    private Table initDynamoDbClient() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_EAST_2)
                .build();
        DynamoDB dynamoDB = new DynamoDB(client);
        String tableName = "PersonDetails";
        return dynamoDB.getTable(tableName);
    }
}
