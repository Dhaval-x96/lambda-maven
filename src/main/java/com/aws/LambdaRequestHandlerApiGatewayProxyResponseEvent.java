package com.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class LambdaRequestHandlerApiGatewayProxyResponseEvent implements RequestHandler<Person, Person> {
    @Override
    public Person handleRequest(Person value, Context context) {
//        try {
//            ObjectMapper objectMapper=new ObjectMapper();
//            person = objectMapper.readValue(value, Person.class);
//        } catch (JsonMappingException e) {
//            e.printStackTrace();
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
        return value;
    }

    public static void main(String[] args)throws Exception {
        String s="{\n  \"address\" : \"Rajpipla\",\n  \"name\" : \"Dhaval\",\n  \"id\" : 2,\n  \"age\" : 25\n}";
        ObjectMapper objectMapper=new ObjectMapper();
        System.out.println(objectMapper.readValue(s, Person.class));
//        Person person = (Person) new JSONParser().parse(s);
//        System.out.println(person);
    }
}
