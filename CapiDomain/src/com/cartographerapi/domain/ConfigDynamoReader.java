package com.cartographerapi.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient; 

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

/**
 * Reader repository for ConfigurationSettings from a DynamoDB table.
 * 
 * @see ConfigReader
 * 
 * @author GodlyPerfection
 *
 */
public class ConfigDynamoReader implements ConfigReader {

    private AmazonDynamoDBClient client;
    private DynamoDBMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue(String key) {
        return mapper.load(ConfigurationSetting.class, key).getValue();
    }
    
    /**
     * The lazy IOC constructor.
     */
    public ConfigDynamoReader() {
        client = new AmazonDynamoDBClient();                                   
        client.setRegion(Region.getRegion(Regions.US_WEST_2));
        mapper = new DynamoDBMapper(client);
    }

}
