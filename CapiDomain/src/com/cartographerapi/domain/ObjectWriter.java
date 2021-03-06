package com.cartographerapi.domain;

import java.util.List;

/**
 * Writer repository interface for Objects. This provides access to a
 * data source that contains the Object.
 * 
 * @author GodlyPerfection
 *
 */
public interface ObjectWriter {
	
	/**
	 * Save Object.
	 * 
	 * @param object
	 * @return
	 */
	public Object saveObject(Object object);
	
	/**
	 * Save multiple Objects.
	 * 
	 * @param objects
	 * @return
	 */
	public List<Object> saveObjects(List<Object> objects);

}
