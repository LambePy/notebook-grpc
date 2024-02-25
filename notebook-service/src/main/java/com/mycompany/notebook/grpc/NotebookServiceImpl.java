package com.mycompany.notebook.grpc;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycompany.notebook.api.v1.NotebookPb;
import com.mycompany.notebook.api.v1.NotebookPb.CreateNoteResponse;
import com.mycompany.notebook.api.v1.NotebookPb.GetNotesResponse;
import com.mycompany.notebook.api.v1.NotebookServiceGrpc;
import com.mycompany.notebook.model.Note;
import com.mycompany.notebook.service.AddNoteService;
import com.mycompany.notebook.service.QueryNotesService;

import io.grpc.stub.StreamObserver;

public class NotebookServiceImpl extends NotebookServiceGrpc.NotebookServiceImplBase {

	private static final Logger logger = LoggerFactory.getLogger(NotebookServiceImpl.class);

	private final AddNoteService addNoteService;

	private final QueryNotesService queryNotesService;

	@Inject
	public NotebookServiceImpl(AddNoteService addNoteService, QueryNotesService queryNotesService) {
		this.addNoteService = addNoteService;
		this.queryNotesService = queryNotesService;
	}

	@Override
	public void createNote(
		NotebookPb.CreateNoteRequest request,
		StreamObserver<NotebookPb.CreateNoteResponse> resObserver
	)
	{
		try {

			String response = addNoteService.addNote(ProtoMapper.INSTANCE.fromProto(request));

			resObserver.onNext(CreateNoteResponse.newBuilder().setMessage(response).build());
			resObserver.onCompleted();
		} catch (Exception e) {
			logger.error("Error handling gRPC call to createNote", e.getMessage());
			resObserver.onError(ExceptionHandler.handleGrpcException(e));
		}
	}

	@Override
	public void getNotes(
		NotebookPb.GetNotesRequest request,
		StreamObserver<NotebookPb.GetNotesResponse> resObserver
	)
	{
		try {
			Note note = queryNotesService.notesByTopic(request.getTopic());

			resObserver
				.onNext(
					GetNotesResponse
						.newBuilder()
						.setNote(ProtoMapper.INSTANCE.toProto(note))
						.build()
				);
			resObserver.onCompleted();
		} catch (Exception e) {
			logger.error("Error handling gRPC call to getNotes", e.getMessage());
			resObserver.onError(ExceptionHandler.handleGrpcException(e));
		}
	}

}
