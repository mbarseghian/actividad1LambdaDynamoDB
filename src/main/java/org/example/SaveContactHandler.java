package org.example;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;

import java.util.HashMap;
import java.util.Map;

public class SaveContactHandler implements RequestHandler<ContactRequest, ContactResponse> {

    private AmazonDynamoDB amazonDynamoDB;

    private String DYNAMODB_TABLE_NAME = "Contacts";
    private Regions REGION = Regions.US_EAST_1;

    public ContactResponse handleRequest(ContactRequest contactRequest, Context context) {
        this.initDynamoDbClient();

        persistData(contactRequest);

        ContactResponse contactResponse = new ContactResponse();
        contactResponse.setMessage("Saved Successfully!!!");
        return contactResponse;
    }

    private void persistData(ContactRequest contactRequest) throws ConditionalCheckFailedException {

        Map<String, AttributeValue> attributesMap = new HashMap<>();

        attributesMap.put("id", new AttributeValue(contactRequest.getId()));
        attributesMap.put("firstName", new AttributeValue(contactRequest.getFirstName()));
        attributesMap.put("lastName", new AttributeValue(contactRequest.getLastName()));
        attributesMap.put("status", new AttributeValue("CREATED"));

        amazonDynamoDB.putItem(DYNAMODB_TABLE_NAME, attributesMap);
    }

    private void initDynamoDbClient() {
        this.amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withRegion(REGION)
                .build();
    }
}