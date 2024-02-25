package com.mycompany.notebook.di;

import com.mycompany.notebook.dao.NotebookDao;
import com.mycompany.notebook.grpc.NotebookServiceImpl;
import com.mycompany.notebook.service.AddNoteService;
import com.mycompany.notebook.service.QueryNotesService;
import com.mycompany.notebook.service.WikiSearchService;

import dagger.Module;
import dagger.Provides;

@Module
public class NotebookServiceModule {

	@Provides
	NotebookDao provideNotebookDao() {
		return new NotebookDao();
	}

	@Provides
	AddNoteService provideAddNoteService(
		NotebookDao notebookDao,
		WikiSearchService wikiSearchService

	)
	{
		return new AddNoteService(notebookDao, wikiSearchService);
	}

	@Provides
	NotebookServiceImpl provideNotebookServiceImpl(
		AddNoteService addNoteservice,
		QueryNotesService queryNotesService
	)
	{
		return new NotebookServiceImpl(addNoteservice, queryNotesService);
	}

	@Provides
	QueryNotesService provideQueryNotesService(NotebookDao notebookDao) {
		return new QueryNotesService(notebookDao);
	}

	@Provides
	WikiSearchService provideWikiSearchService() {
		return new WikiSearchService();

	}

}
