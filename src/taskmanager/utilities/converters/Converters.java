package taskmanager.utilities.converters;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;

@SuppressWarnings({"UnusedReturnValue", "WeakerAccess"})
public class Converters {

    public static final Type DURATION_TYPE = new TypeToken<Duration>() {
    }.getType();

    public static final Type LOCAL_DATE_TIME_TYPE = new TypeToken<LocalDateTime>() {
    }.getType();

    public static GsonBuilder registerAll(GsonBuilder builder) {
        if (builder == null) {
            throw new NullPointerException("builder cannot be null");
        }
        registerLocalDateTime(builder);
        registerDuration(builder);
        return builder;
    }

    public static GsonBuilder registerLocalDateTime(GsonBuilder builder) {
        builder.registerTypeAdapter(LOCAL_DATE_TIME_TYPE, new LocalDateTimeConverter());
        return builder;
    }

    public static GsonBuilder registerDuration(GsonBuilder builder) {
        builder.registerTypeAdapter(DURATION_TYPE, new DurationConverter());
        return builder;
    }

}
