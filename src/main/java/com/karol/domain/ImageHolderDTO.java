package com.karol.domain;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.karol.domain.mappers.MyDateSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize(using = MyDateSerializer.class)
public class ImageHolderDTO {
	private Long id;
	private String description;
	private int width;
	private int height;
	private String username;
	private boolean isPublic;
	private ZonedDateTime timestamp;
	private List<Link> links = new ArrayList<Link>();
	
	public ImageHolderDTO addLink(Link ...links) {
		Arrays.asList(links).forEach(this.links::add);
		return this;
	}
	public ImageHolderDTO convertToGivenTimeZone(TimeZone tz) {
		
		if(this.getTimestamp() !=null) {
			this.setTimestamp(this.getTimestamp().withZoneSameInstant(tz.toZoneId()));
		}
		return this;
	}
}
