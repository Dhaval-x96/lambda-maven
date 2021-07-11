package com.aws;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.Serializable;

public class LambdaRequestHandler implements RequestHandler<Person, String> {

    @Override
    public String handleRequest(Person person, Context context) {
        try {
            context.getLogger().log("input:-"+ person);
            Table table = this.initDynamoDbClient();
           table.putItem(
                    new PutItemSpec().withItem(new Item()
                            .withInt("Id",person.getId())
                            .withString("name", person.getName())
                            .withString("address", person.getAddress())
                            .withInt("age",person.getAge())));
            return "Save Successfully";
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
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

class Person implements Serializable {
    private Integer Id;
    private String name;
    private String address;
    private Integer age;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", age=" + age +
                '}';
    }
}
