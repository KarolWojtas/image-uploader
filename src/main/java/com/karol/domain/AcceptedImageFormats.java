package com.karol.domain;

public enum AcceptedImageFormats {
	JPG("jpg"),JPEG("jpeg"),PNG("png");
	String value;

	private AcceptedImageFormats(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
