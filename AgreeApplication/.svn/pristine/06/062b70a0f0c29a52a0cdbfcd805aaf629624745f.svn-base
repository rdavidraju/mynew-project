package com.nspl.app.web.rest.util;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class LocalDateTypeConverter
implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

@Override
public JsonElement serialize(LocalDate src, Type srcType, JsonSerializationContext context) {
return new JsonPrimitive(src.toString());
}

@Override
public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext context)
  throws JsonParseException {
	 Date date = context.deserialize(json, Date.class);
	 ZoneId defaultZoneId = ZoneId.systemDefault();
	    LocalDate localDate=null;
	    Instant instant = date.toInstant();
		  localDate = instant.atZone(defaultZoneId).toLocalDate();
	
	return localDate;
	
	
}

}