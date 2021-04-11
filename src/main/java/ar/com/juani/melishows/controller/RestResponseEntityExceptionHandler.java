package ar.com.juani.melishows.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import ar.com.juani.melishows.dto.ApiErrorDto;
import ar.com.juani.melishows.exception.NotAvailableSeatsException;
import ar.com.juani.melishows.exception.NotAvailableSeatsLockException;
import ar.com.juani.melishows.exception.ShowingNotFoundException;
import ar.com.juani.melishows.exception.StringToEnumConverterException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(NotAvailableSeatsException.class)
	public ResponseEntity<Object> handleNotAvailableSeatsException(RuntimeException ex, WebRequest request) {
		return handleSeatAvailabilityException(ex);
	}

	@ExceptionHandler(NotAvailableSeatsLockException.class)
	public ResponseEntity<Object> handleNotAvailableSeatsLockException(RuntimeException ex, WebRequest request) {
		return handleSeatAvailabilityException(ex);
	}

	// TODO due to the issue with the connection released in @CommandServiceImpl this handling must be done. 
	// Needs to be removed when the previous got fixed 
	@ExceptionHandler(org.springframework.orm.jpa.JpaSystemException.class)
	public ResponseEntity<Object> handleNotAvailableSeatsLock2Exception(RuntimeException ex, WebRequest request) {
		return handleSeatAvailabilityException(ex);
	}

	private ResponseEntity<Object> handleSeatAvailabilityException(RuntimeException ex) {
		ApiErrorDto apiError = new ApiErrorDto(HttpStatus.CONFLICT, ex);
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

	@ExceptionHandler(StringToEnumConverterException.class)
	public ResponseEntity<Object> handleConversionException(RuntimeException ex) {
		ApiErrorDto apiError = new ApiErrorDto(HttpStatus.BAD_REQUEST, ex);
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

	@ExceptionHandler(ShowingNotFoundException.class)
	public ResponseEntity<Object> handleShowingNotFoundException(RuntimeException ex) {
		ApiErrorDto apiError = new ApiErrorDto(HttpStatus.NOT_FOUND, ex);
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		if (body != null) {
			return super.handleExceptionInternal(ex, body, headers, status, request);
		} else {
			ApiErrorDto apiError = new ApiErrorDto(status, ex);
			return new ResponseEntity<>(apiError, apiError.getStatus());

		}
	}
}