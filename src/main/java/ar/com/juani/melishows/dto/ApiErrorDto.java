package ar.com.juani.melishows.dto;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ApiErrorDto {
	
	   private HttpStatus status = null;
	   private Integer statusCode = null;
	   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	   private LocalDateTime timestamp;
	   private String message = "";
	   private String debugMessage = "";
	
	   private ApiErrorDto() {
	       timestamp = LocalDateTime.now();
	   }
	
	   public ApiErrorDto(HttpStatus status) {
	       this();
	       this.status = status;
	       this.statusCode = status.value();
	   }
	
	   public ApiErrorDto(HttpStatus status, Throwable ex) {
	       this();
	       this.status = status;
	       this.statusCode = status.value();
	       this.message = ex.getClass().getSimpleName();
	       this.debugMessage = ex.getLocalizedMessage();
	   }
	
	   public ApiErrorDto(HttpStatus status, String message, Throwable ex) {
	       this();
	       this.status = status;
	       this.statusCode = status.value();
	       this.message = message;
	       this.debugMessage = ex.getLocalizedMessage();
	   }
}