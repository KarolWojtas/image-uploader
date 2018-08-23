package com.karol.domain.mappers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.karol.domain.ImageHolderDTO;

public class MyDateSerializer extends StdSerializer<ImageHolderDTO>{

	public MyDateSerializer() {
		this(null);
	}
	public MyDateSerializer(Class<ImageHolderDTO> t) {
		super(t);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void serialize(ImageHolderDTO dto, JsonGenerator jgen, SerializerProvider sp) throws IOException {
			jgen.writeStartObject();
			jgen.writeNumberField("id", dto.getId());
			jgen.writeStringField("description", dto.getDescription());
			jgen.writeStringField("username", dto.getUsername());
			jgen.writeBooleanField("isPublic", dto.isPublic());
			jgen.writeObjectField("timestamp", dto.getTimestamp().toInstant().toEpochMilli());
			jgen.writeObjectField("links", dto.getLinks());
			jgen.writeEndObject();
		
	}

}
