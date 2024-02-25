package com.mycompany.notebook.dao;

import java.io.File;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycompany.notebook.model.Notes;

public class NotebookDao {

	private static final Logger logger = LoggerFactory.getLogger(NotebookDao.class);
	private static final String FILE_PATH = "src/main/resources/notebook.xml";

	@Inject
	public NotebookDao() {

	}

	public void saveNote(Notes notes) {
		try {
			JAXBContext context = JAXBContext.newInstance(Notes.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			File file = new File(FILE_PATH);
			marshaller.marshal(notes, file);
		} catch (JAXBException e) {
			logger.error("Failed to save data to notebook.xml", e);
			throw new RuntimeException("Failed to save data to notebook.xml", e);
		}
	}

	public Notes fetchNotes() {
		File file = new File(FILE_PATH);
		if (!file.exists()) {
			return new Notes();
		}
		try {
			JAXBContext context = JAXBContext.newInstance(Notes.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (Notes) unmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			logger.error("Failed to load data from notebook.xml", e);
			return new Notes();
		}
	}

}
