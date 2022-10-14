package pl.macia.multiplex.core;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.google.gson.*;
/**
 * JsonDeserializer<LocalDateTime>.
 * @author CodeServant
 *
 */
public class JsonDateDeserialize implements JsonDeserializer<LocalDateTime> {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	@Override
	public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		return LocalDateTime.parse(json.getAsString(),
				DateTimeFormatter.ofPattern("d-MMM-yyyy").withLocale(Locale.ENGLISH));
	}
}
