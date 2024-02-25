package com.mycompany.notebook.model;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "Note")
@XmlAccessorType(XmlAccessType.FIELD)
public class Note {

	@XmlElement(name = "Topic")
	private String topic;

	@XmlElement(name = "Text")
	private String text;

	@XmlElement(name = "Timestamp")
	@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
	private LocalDateTime timestamp;

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

}
