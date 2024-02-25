package com.mycompany.notebook.di;

import javax.inject.Singleton;

import com.mycompany.notebook.grpc.NotebookServiceImpl;

import dagger.Component;

@Singleton
@Component(modules = NotebookServiceModule.class)
public interface NotebookServiceComponent {

	NotebookServiceImpl getNotebookServiceImpl();

}
