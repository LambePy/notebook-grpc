package com.mycompany.notebook.service;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycompany.notebook.dao.NotebookDao;
import com.mycompany.notebook.exception.NotebookServiceException;
import com.mycompany.notebook.model.Note;
import com.mycompany.notebook.model.Notes;

public class AddNoteService {

	private static final Logger logger = LoggerFactory.getLogger(AddNoteService.class);
	private final NotebookDao notebookDao;
	private final WikiSearchService wikiSearchService;

	@Inject
	public AddNoteService(NotebookDao notebookDao, WikiSearchService wikiSearchService) {
		this.notebookDao = notebookDao;
		this.wikiSearchService = wikiSearchService;
	}

	public String addNote(Note newNote) {
		try {
			Notes existingNotes = notebookDao.fetchNotes();
			existingNotes
				.getNotes()
				.stream()
				.filter(notes -> notes.getTopic().equalsIgnoreCase(newNote.getTopic()))
				.findFirst()
				.ifPresentOrElse(existingNote ->
				{
					String updatedText = existingNote.getText() + " " + newNote.getText();
					existingNote.setText(updatedText);
					logger
						.info(
							"Existing note with topic: {} found, appending text",
							newNote.getTopic()
						);
				}, () -> {
					String additionalInfo = wikiSearchService
						.searchWikiForMoreInformation(newNote.getTopic());
					if (additionalInfo != null && !additionalInfo.isEmpty()) {
						newNote.setText(newNote.getText() + additionalInfo);
					}
					existingNotes.addNote(newNote);
					logger.info("Adding a new note with topic: {}", newNote.getTopic());

				});
			notebookDao.saveNote(existingNotes);
			return "Note added succesfully";
		} catch (Exception e) {
			logger.error("Error adding a new note", e);
			throw new NotebookServiceException("Error adding a new note", e);
		}
	}

}