# MongoDb Appender for Logback

Just another mongodb appender for logback

## Dependencies

- mongodb driver

## Usage

Add to your logback.xml:

    <appender name="mongoDbAppender" class="mongodbappender.mongoappender.MongoAppender">
        <uri>mongodb://localhost:27017</uri>
        <database>teste</database>
        <collection>logs</collection>
    </appender>
    
    <logger name="mongoLogger">
        <appender-ref ref="mongoDbAppender" />
    </logger>

## Behavior
will save anything that is in the argument array passed to the log. At first
will try to cast the item in the array to a Map<String, ?> but if it fails
will cast to a String.

Because it uses ".insertMany()" to save in the database if one fails it'll save
everything as a String, so I recommend passing only Hash tables to it.

If you pass only a message, it will not save.