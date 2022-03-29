package taskmanager.utilities.converters;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Duration;

public class DurationConverter implements JsonSerializer<Duration>, JsonDeserializer<Duration> {

    @Override
    public Duration deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        String src = jsonElement.getAsString();
        int hours = Integer.parseInt(src);
        return Duration.ofHours(hours);
    }

    @Override
    public JsonElement serialize(Duration duration, Type type, JsonSerializationContext context) {
        int hours = (int) duration.toHours();
        return new JsonPrimitive(hours);
    }
}
