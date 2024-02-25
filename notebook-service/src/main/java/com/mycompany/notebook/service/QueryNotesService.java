package com.mycompany.notebook.service;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycompany.notebook.dao.NotebookDao;
import com.mycompany.notebook.exception.NotebookServiceException;
import com.mycompany.notebook.exception.ServiceStatusException;
import com.mycompany.notebook.model.Note;
import com.mycompany.notebook.model.Notes;

import io.grpc.Status;

public class QueryNotesService {

	private final NotebookDao notebookDao;
	private static final Logger logger = LoggerFactory.getLogger(QueryNotesService.class);

	@Inject
	public QueryNotesService(NotebookDao notebookDao) {
		this.notebookDao = notebookDao;
	}

	public Note notesByTopic(String topic) {

		try {
			Notes notes = notebookDao.fetchNotes();
			return notes
				.getNotes()
				.stream()
				.filter(note -> note.getTopic().equalsIgnoreCase(topic))
				.findFirst()
				.map(note ->
				{
					logger.info("Note found with topic '{}'", topic);
					return note;
				})
				.orElseThrow(
					() -> new ServiceStatusException(
						Status.NOT_FOUND,
						String.format("No notes found with '%s'", topic)
					)
				);

		} catch (ServiceStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new NotebookServiceException("Failed to fetch notes", e);
		}
	}

}
