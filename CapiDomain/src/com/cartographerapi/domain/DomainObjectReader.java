package com.cartographerapi.domain;

import java.util.List;
import java.util.Map;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

/**
 * Reader repository interface for domain Objects. This provides access to a
 * data source that contains the domain Object.
 * 
 * @author GodlyPerfection
 *
 */
public interface DomainObjectReader {
	
	/**
	 * Get multiple domain Objects from a single segment page.
	 * 
	 * @param objects
	 * @return
	 */
	public List<Object> getAPageOfDomainObjects(SegmentScannerRequest request);
	
	/**
	 * Get the last evaluated key of the page grabbed.
	 * 
	 * @return
	 */
	public Map<String, AttributeValue> getLastEvaluatedKey();

}
