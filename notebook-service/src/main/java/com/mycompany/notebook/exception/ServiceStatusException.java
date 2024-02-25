package com.mycompany.notebook.exception;

import java.io.Serial;

import io.grpc.Status;

public class ServiceStatusException extends NotebookServiceException {

	private final transient Status status;

	@Serial
	private static final long serialVersionUID = 1L;

	public ServiceStatusException(Status status, String message) {
		super(message);
		this.status = status;
	}

	public Status getStatus() {
		return status;
	}

}
