package mongodb;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;


/**
 *  This program will connect to Mongo Server and fetch all the databases name.
 *  For particular data base, we are fetching all the collection.
 *  
 *   
 *   Add below dependency in pom.xml
 *     <dependencies>
 *        <dependency>
 *           <groupId>org.mongodb</groupId>
 *          <artifactId>mongo-java-driver</artifactId>
 *          <version>3.8.0</version>
 *      </dependency>
 *  </dependencies>
 *
 */

public class MongoDbClientEx {
	public static void main(String args[]) throws IOException {
		mongoDbConnectionUsingMongoClient();
	}

	private static void mongoDbConnectionUsingMongoClient() throws FileNotFoundException, IOException {
		MongoClient mongoClient = MongoClients.create("mongodb://localhost");
		System.out.println("successful");
		
		MongoIterable<String> dbs = mongoClient.listDatabaseNames();
		if (dbs == null) {
			System.out.println("no dbs");
		}
		List<String> tables = new ArrayList<String>();
		MongoCursor<String> cursor = dbs.iterator();
		while(cursor.hasNext()){
			String table = cursor.next();
			tables.add(table);
		}
		System.out.println("db names: " + tables);
		

		MongoDatabase database = mongoClient.getDatabase("mydb");
		System.out.println("database" + database.getName());
		
		
		MongoCollection<Document> collSubs = database.getCollection("collSubs");
		MongoCollection<Document> collConf = database.getCollection("collConf");
		System.out.println("collection1: " + collSubs);
		System.out.println("collection1: " + collConf);
		
	} 

}
