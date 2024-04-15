package mongoappender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.status.ErrorStatus;
import org.bson.Document;

import java.util.Arrays;
import java.util.Map;

public class MongoAppender extends AppenderBase<ILoggingEvent> {
    private String uri;
    private String database;
    private String collection;
    private MongoConnection connection;

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    @Override
    public void start() {
        String connectionString = uri + "/" + database + "." + collection;
        try {
             this.connection = new MongoConnection(connectionString);
        } catch (Exception e ) {
            addStatus(new ErrorStatus("Failed to connect to " + connectionString, this, e));
            return;
        }
        super.start();
    }

    @Override
    public void stop() {
        this.connection.close();
        super.stop();
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        try {
            if (eventObject.getArgumentArray() != null) {
                connection.getCollection().insertMany(Arrays.stream(eventObject.getArgumentArray()).map(item ->
                        new Document((Map<String, ?>) item)
                ).toList());
            }
        } catch (ClassCastException e ) {
            if (eventObject.getArgumentArray() != null) {
                connection.getCollection().insertMany(Arrays.stream(eventObject.getArgumentArray()).map(item ->
                        new Document("message", item.toString())
                ).toList());
            }
        }
    }
}
