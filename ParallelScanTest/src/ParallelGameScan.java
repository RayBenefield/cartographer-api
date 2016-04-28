import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient; 
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

import com.cartographerapi.domain.ObjectSqsWriter;
import com.cartographerapi.domain.game.Game;
import com.cartographerapi.domain.game.GamesQueueReader;
import com.cartographerapi.domain.game.GamesSqsReader;

import java.util.List;
import java.util.ArrayList;

public class ParallelGameScan {

	public static void main(String[] args) {
		try {
//			AmazonDynamoDBClient client = new AmazonDynamoDBClient();
//			client.setRegion(Region.getRegion(Regions.US_WEST_2));
//			DynamoDBMapper dbMapper = new DynamoDBMapper(client);
//			
//			DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
//			List<Object> games = new ArrayList<Object>(dbMapper.parallelScan(Game.class, scanExpression, 3));
//			
//			GamesSqsWriter writer = new GamesSqsWriter("sqsTestScan");
//			writer.saveGames(games);

			ObjectMapper mapper = new ObjectMapper();
			GamesSqsReader reader = new GamesSqsReader("sqsTestScan");
			List<Game> games = reader.getNumberOfGames(10);
			
			String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(games);
			String json = mapper.writeValueAsString(games);

//			System.out.println(prettyJson);
			Integer i = 1;
			for (Game aGame : games) {
				System.out.println(i + ": " + aGame.getGameData());
//				System.out.println(i + ": " + mapper.writeValueAsString(aGame));
				i++;
			}
		} catch (IOException exception) {
			System.out.println("oops");
		}
	}

}
