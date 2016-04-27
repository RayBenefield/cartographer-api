import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient; 
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import com.cartographerapi.domain.game.Game;
import java.util.List;

public class ParallelGameScan {

	public static void main(String[] args) {
		try {
			AmazonDynamoDBClient client = new AmazonDynamoDBClient();
			client.setRegion(Region.getRegion(Regions.US_WEST_2));
			DynamoDBMapper dbMapper = new DynamoDBMapper(client);
			ObjectMapper mapper = new ObjectMapper();
			
			Game game = dbMapper.load(Game.class, "1bbe2aa0-d0c1-4bf4-bcdd-754c8720b95b");
			DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
			List<Game> games = dbMapper.parallelScan(Game.class, scanExpression, 3);
			
			String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(games);
			String json = mapper.writeValueAsString(game);

//			System.out.println(prettyJson);
			Integer i = 1;
			for (Game aGame : games) {
				System.out.println(i + ": " + aGame.getMatchId());
				i++;
			}
		} catch (IOException exception) {
			System.out.println("oops");
		}
	}

}
