package pl.macia.multiplex.core;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.*;
import java.lang.reflect.Type;

/**
 * JsonSerializer<LocalDateTime>.
 * @author CodeServant
 *
 */
public class JsonDateSerialize implements JsonSerializer<LocalDateTime> {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	@Override
	public JsonElement serialize(LocalDateTime localDate, Type srcType, JsonSerializationContext context) {
		return new JsonPrimitive(formatter.format(localDate));
	}
}
