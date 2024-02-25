package com.mycompany.notebook.grpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycompany.notebook.exception.ServiceStatusException;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;

public class ExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

	private ExceptionHandler() {
	}

	public static StatusRuntimeException handleGrpcException(Throwable throwable) {

		return switch (throwable) {
		case ServiceStatusException e -> {
			logger.warn(e.getMessage());
			yield new StatusRuntimeException(e.getStatus().withDescription(e.getMessage()));
		}

		default -> {
			logger.error(throwable.getMessage(), throwable);
			yield new StatusRuntimeException(
				Status.INTERNAL.withDescription("Internal Server Error")
			);
		}

		};

	}

}
