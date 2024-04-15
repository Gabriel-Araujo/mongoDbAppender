package mongoappender;

import com.mongodb.ConnectionString;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

/**
 * Creates a connection with a mongoDb client.
 * Is necessary to pass a valid connection string that is: uri + database + collection
 *
 * If a nonexistent collection is passed, it'll create a new one.
 */
public class MongoConnection {
    private final ConnectionString connectionString;
    private final MongoClient client;

    /**
     * Creates a client connection with the mongoDb using the information passed.
     * @param connectionString a string containing uri + database + collection
     */
    public MongoConnection(final String connectionString) {
        this.connectionString = new ConnectionString(connectionString);

        this.client = MongoClients.create(this.connectionString);
    }

    public void close() {
        client.close();
    }

    /**
     * @return a reference to a valid collection.
     */
    public MongoCollection<Document> getCollection() {
        try {
            return this.client.getDatabase(this.connectionString.getDatabase()).getCollection(this.connectionString.getCollection());
        } catch (final MongoException e) {
            this.client.getDatabase(this.connectionString.getDatabase()).createCollection(this.connectionString.getCollection());
            return this.client.getDatabase(this.connectionString.getDatabase()).getCollection(this.connectionString.getCollection());
        }
    }
}
