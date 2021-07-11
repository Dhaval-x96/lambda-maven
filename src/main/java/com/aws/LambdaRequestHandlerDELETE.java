package com.aws;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class LambdaRequestHandlerDELETE implements RequestHandler<Map<String,Integer>, Person> {
    @Override
    public Person handleRequest(Map<String,Integer> data, Context context) {
        Table table = this.initDynamoDbClient();

        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey("Id", data.get("Id")).withReturnValues(ReturnValue.ALL_OLD);

        DeleteItemOutcome deleteItemOutcome =  table.deleteItem(deleteItemSpec);
        ObjectMapper objectMapper=new ObjectMapper();
        Person response= null;
        try {
            response= objectMapper.readValue(deleteItemOutcome.getItem().toJSONPretty().replace("Id","id"), Person.class);
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
        String tableName ="PersonDetails";
        return dynamoDB.getTable(tableName);
    }
}
