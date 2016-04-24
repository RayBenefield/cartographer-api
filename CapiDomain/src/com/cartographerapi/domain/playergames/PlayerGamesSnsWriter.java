package com.cartographerapi.domain.playergames;

import java.util.List;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.model.PublishRequest;

/**
 * Writer repository for PlayerGames to an SNS topic.
 * 
 * @see PlayerGamesWriter
 * 
 * @author GodlyPerfection
 *
 */
public class PlayerGamesSnsWriter implements PlayerGamesWriter {
	
	private String topicArn;
	private AmazonSNSClient snsClient;
	private ObjectMapper mapper;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PlayerGame> savePlayerGames(List<PlayerGame> games) {
		// TODO clean up try catch
		try {
			for (PlayerGame game : games) {
				PublishRequest publishRequest;
				publishRequest = new PublishRequest(topicArn, mapper.writeValueAsString(game));
				snsClient.publish(publishRequest);
			}
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return games;
	}
	
    /**
     * The lazy IOC constructor.
     */
	public PlayerGamesSnsWriter(String topicArn) {
		this.topicArn = topicArn;
        mapper = new ObjectMapper();
        snsClient = new AmazonSNSClient();		                           
        snsClient.setRegion(Region.getRegion(Regions.US_WEST_2));
	}

}
