package com.mycompany.notebook.grpc;

import java.time.LocalDateTime;

import com.google.type.DateTime;
import com.mycompany.notebook.api.v1.NotebookPb;
import com.mycompany.notebook.api.v1.NotebookPb.CreateNoteRequest;
import com.mycompany.notebook.model.Note;

public class ProtoMapper {

	public static final ProtoMapper INSTANCE = new ProtoMapper();

	private ProtoMapper() {
	}

	public NotebookPb.Note toProto(Note note) {
		NotebookPb.Note.Builder builder = NotebookPb.Note
			.newBuilder()
			.setText(note.getText())
			.setTopic(note.getTopic())
			.setTimestamp(localDateTimeToProto(note.getTimestamp()));
		return builder.build();

	}

	public Note fromProto(CreateNoteRequest request) {
		Note note = new Note();

		note.setText(request.getText());
		note.setTopic(request.getTopic());
		note.setTimestamp(LocalDateTime.now());
		return note;
	}

	private DateTime localDateTimeToProto(LocalDateTime localDate) {
		return DateTime
			.newBuilder()
			.setYear(localDate.getYear())
			.setMonth(localDate.getMonthValue())
			.setDay(localDate.getDayOfMonth())
			.setHours(localDate.getHour())
			.setMinutes(localDate.getMinute())
			.setSeconds(localDate.getSecond())
			.build();
	}

}
