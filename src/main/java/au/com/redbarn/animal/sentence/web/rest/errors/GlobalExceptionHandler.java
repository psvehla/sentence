package au.com.redbarn.animal.sentence.web.rest.errors;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MultipartException.class)
	public String handleMultipartException(MultipartException e) {
		return "There is a problem with the file you tried to upload. Please try another one.";
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
		return "The file you tried to upload is too big. Please try another one, smaller than 1MB.";
	}
}
