package com.mycompany.notebook;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycompany.notebook.di.DaggerNotebookServiceComponent;
import com.mycompany.notebook.di.NotebookServiceComponent;
import com.mycompany.notebook.grpc.NotebookServiceImpl;

import io.grpc.ServerBuilder;

public class Bootstrap {

	private static final String SERVICE_NAME = "notebook";
	private static final int PORT = 8080;

	private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

	public static void main(String[] args) throws IOException, InterruptedException {

		NotebookServiceComponent component = DaggerNotebookServiceComponent.create();
		NotebookServiceImpl serviceImpl = component.getNotebookServiceImpl();

		io.grpc.Server server = ServerBuilder.forPort(8080).addService(serviceImpl).build();

		server.start();
		logger.info("Service {} started on port {}", SERVICE_NAME, PORT);
		server.awaitTermination();
	}

}
