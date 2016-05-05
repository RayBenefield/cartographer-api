package com.cartographerapi.domain;

/**
 * Reader repository interface for domain Objects. This provides access to a
 * data source that contains the domain Object.
 * 
 * @author GodlyPerfection
 *
 */
public interface ConfigReader {

	/**
	 * Get a configuration value based on a key.
	 * 
	 * @param key
	 * @return
	 */
	public String getValue(String key);

}
