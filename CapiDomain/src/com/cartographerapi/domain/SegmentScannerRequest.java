package com.cartographerapi.domain;

import java.util.Map;

/**
 * Domain Object that represents a SegmentScannerRequest.
 * 
 * @author GodlyPerfection
 *
 */
public class SegmentScannerRequest {

	private String domainObject;
	private String queueUrlKey;
	private Integer segmentId;
	private Integer totalSegments;
	private Map<String, Object> lastEvaluatedKey;

	public String getQueueUrlKey() {
		return queueUrlKey;
	}

	public void setQueueUrlKey(String queueUrlKey) {
		this.queueUrlKey = queueUrlKey;
	}
	
	public String getDomainObject() {
		return domainObject;
	}

	public void setDomainObject(String domainObject) {
		this.domainObject = domainObject;
	}

	public Integer getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(Integer segmentId) {
		this.segmentId = segmentId;
	}
	
	public Integer getTotalSegments() {
		return totalSegments;
	}

	public void setTotalSegments(Integer totalSegments) {
		this.totalSegments = totalSegments;
	}

	public Map<String, Object> getLastEvaluatedKey() {
		return lastEvaluatedKey;
	}

	public void setLastEvaluatedKey(Map<String, Object> lastEvaluatedKey) {
		this.lastEvaluatedKey = lastEvaluatedKey;
	}
	
	public SegmentScannerRequest(Integer segmentId, Integer totalSegments, String domainObject, String queueUrlKey) {
		this.segmentId = segmentId;
		this.totalSegments = totalSegments;
		this.domainObject = domainObject;
		this.queueUrlKey = queueUrlKey;
	}
	
	public SegmentScannerRequest(Integer segmentId, Integer totalSegments, String domainObject, String queueUrlKey, Map<String, Object> lastEvaluatedKey) {
		this.segmentId = segmentId;
		this.totalSegments = totalSegments;
		this.domainObject = domainObject;
		this.queueUrlKey = queueUrlKey;
		this.lastEvaluatedKey = lastEvaluatedKey;
	}

	@SuppressWarnings("unchecked")
	public SegmentScannerRequest(Map<String, Object> map) {
		this(
			(Integer)map.get("segmentId"),
			(Integer)map.get("totalSegments"),
			(String)map.get("domainObject"),
			(String)map.get("queueUrlKey"),
			(Map<String, Object>)map.get("lastEvaluatedKey")
		);
	}
	
	public SegmentScannerRequest() {
	}

}
