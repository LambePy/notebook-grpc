package com.mycompany.notebook.exception;

import java.io.Serial;

public class NotebookServiceException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;

	public NotebookServiceException(String message) {
		super(message);
	}

	public NotebookServiceException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
