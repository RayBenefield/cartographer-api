package com.cartographerapi.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * Domain object that handles a configuration setting.
 * <pre>
 *    Key
 *    Value
 * </pre>
 * 
 * @author GodlyPerfection
 *
 */
@DynamoDBTable(tableName="Config")
public class ConfigurationSetting {

	private String key;
	private String value;

	@DynamoDBHashKey(attributeName="Key")
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@DynamoDBAttribute(attributeName="Value")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public ConfigurationSetting() {
	}

}
