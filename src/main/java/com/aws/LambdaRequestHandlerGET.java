package com.aws;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class LambdaRequestHandlerGET implements RequestHandler<Map<String, Integer>, Person> {
    @Override
    public Person handleRequest(Map<String,Integer> data, Context context) {
        Table table = this.initDynamoDbClient();
        GetItemSpec getItemSpec = new GetItemSpec().withPrimaryKey("Id",data.get("Id"));
        String strResult = table.getItem(getItemSpec).toJSONPretty().replace("Id","id");
        ObjectMapper objectMapper=new ObjectMapper();
        Person person= null;
        try {
           person= objectMapper.readValue(strResult, Person.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return person;
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
