package com.mycompany.client;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycompany.notebook.api.v1.NotebookPb;
import com.mycompany.notebook.api.v1.NotebookServiceGrpc;
import com.mycompany.notebook.api.v1.NotebookServiceGrpc.NotebookServiceBlockingStub;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class GrpcClient {

	private static final Logger logger = LoggerFactory.getLogger(GrpcClient.class);

	private final NotebookServiceBlockingStub blockingStub;

	public GrpcClient(ManagedChannel channel) {
		blockingStub = NotebookServiceGrpc.newBlockingStub(channel);
	}

	public void addNote(String topic, String text) {
		NotebookPb.CreateNoteRequest request = NotebookPb.CreateNoteRequest
			.newBuilder()
			.setTopic(topic)
			.setText(text)
			.build();
		try {
			NotebookPb.CreateNoteResponse response = blockingStub.createNote(request);
			logger.info(response.getMessage());
		} catch (StatusRuntimeException e) {
			logger
				.error(
					"Call failed with status code: {}, description: {} ",
					e.getStatus().getCode(),
					e.getStatus().getDescription()
				);
		}

	}

	public void getNotesByTopic(String topic) {
		NotebookPb.GetNotesRequest request = NotebookPb.GetNotesRequest
			.newBuilder()
			.setTopic(topic)
			.build();

		try {
			NotebookPb.GetNotesResponse response = blockingStub.getNotes(request);

			logger
				.info(
					"Recieved Note with \n Topic: {} \n Text: {} \n Timestamp: {}",
					response.getNote().getTopic(),
					response.getNote().getText(),
					response.getNote().getTimestamp()
				);
		} catch (StatusRuntimeException e) {
			logger
				.error(
					"Call failed with status code: {}, description: {} ",
					e.getStatus().getCode(),
					e.getStatus().getDescription()
				);
		}

	}

	public static void main(String[] args) throws Exception {
		ManagedChannel channel = ManagedChannelBuilder
			.forAddress("localhost", 8080)
			.usePlaintext()
			.build();
		try {
			GrpcClient client = new GrpcClient(channel);
			Scanner scanner = new Scanner(System.in);

			while (true) {
				System.out
					.println(
						"\n1.Add Note \n2.Get Notes by Topic \n3.Exit app \nChoose an option:"
					);
				int option = scanner.nextInt();
				scanner.nextLine();

				switch (option) {
				case 1:
					System.out.println("Enter topic:");
					String topic = scanner.nextLine();
					System.out.println("Enter text:");
					String text = scanner.nextLine();
					client.addNote(topic, text);
					break;
				case 2:
					System.out.println("Enter topic to fetch:");
					topic = scanner.nextLine();
					client.getNotesByTopic(topic);
					break;
				default:
					System.out.println("Exiting.");
					return;
				}
			}
		} finally {
			channel.shutdownNow();
		}
	}

}
