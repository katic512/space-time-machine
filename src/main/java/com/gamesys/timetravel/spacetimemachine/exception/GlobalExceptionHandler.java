package com.gamesys.timetravel.spacetimemachine.exception;

import static com.gamesys.timetravel.spacetimemachine.constant.AppConstants.UTC;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
/**
 * 
 * @author Karthick Narasimhan
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(value = { DuplicateDataException.class})
	protected ResponseEntity<ExceptionDetail> handleDuplicateDataException(DuplicateDataException ex) {
		String strLogPrefix = "Method : handleDuplicateDataException()";
		LOGGER.info("{} | Entry ", strLogPrefix);
		ExceptionDetail errorDetails = new ExceptionDetail(LocalDateTime.now(ZoneId.of(UTC)), ex.getMessage());
		LOGGER.info("{} | Exit ", strLogPrefix);
		return new ResponseEntity<ExceptionDetail>(errorDetails, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(value = { ParadoxException.class})
	protected ResponseEntity<ExceptionDetail> handleParadoxException(ParadoxException ex) {
		String strLogPrefix = "Method : handleParadoxException()";
		LOGGER.info("{} | Entry ", strLogPrefix);
		ExceptionDetail errorDetails = new ExceptionDetail(LocalDateTime.now(ZoneId.of(UTC)), ex.getMessage());
		LOGGER.info("{} | Exit ", strLogPrefix);
		return new ResponseEntity<ExceptionDetail>(errorDetails, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(value = { DataNotFoundException.class})
	protected ResponseEntity<ExceptionDetail> handleDataNotFoundException(DataNotFoundException ex) {
		String strLogPrefix = "Method : handleDataNotFoundException()";
		LOGGER.info("{} | Entry ", strLogPrefix);
		ExceptionDetail errorDetails = new ExceptionDetail(LocalDateTime.now(ZoneId.of(UTC)), ex.getMessage());
		LOGGER.info("{} | Exit ", strLogPrefix);
		return new ResponseEntity<ExceptionDetail>(errorDetails, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = { HttpMessageNotReadableException.class})
	protected ResponseEntity<ExceptionDetail> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
		String strLogPrefix = "Method : handleHttpMessageNotReadableException()";
		LOGGER.info("{} | Entry ", strLogPrefix);
		ExceptionDetail errorDetails = new ExceptionDetail(LocalDateTime.now(ZoneId.of(UTC)), ex.getMessage());
		LOGGER.info("{} | Exit ", strLogPrefix);
		return new ResponseEntity<ExceptionDetail>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { MethodArgumentNotValidException.class})
	protected ResponseEntity<ExceptionDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		String strLogPrefix = "Method : handleMethodArgumentNotValidException()";
		LOGGER.info("{} | Entry ", strLogPrefix);
		ExceptionDetail errorDetails = new ExceptionDetail(LocalDateTime.now(ZoneId.of(UTC)), null);
		ex.getFieldErrors().forEach((fieldError)->errorDetails.setMessage(fieldError.getDefaultMessage()));
		LOGGER.info("{} | Exit ", strLogPrefix);
		return new ResponseEntity<ExceptionDetail>(errorDetails, HttpStatus.BAD_REQUEST);
	}
}
