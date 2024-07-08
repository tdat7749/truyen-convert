package truyenconvert.server.modules.report.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import truyenconvert.server.commons.ResponseError;

@RestControllerAdvice
public class ReportExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportExceptionHandler.class);

    @ExceptionHandler(ReportTypeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError reportTypeNotFoundExceptionHandler(ReportTypeNotFoundException ex){
        LOGGER.warn(ex.getMessage());
        return new ResponseError(HttpStatus.NOT_FOUND,404,ex.getMessage());
    }

    @ExceptionHandler(ReportNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError reportNotFoundExceptionHandler(ReportNotFoundException ex){
        LOGGER.warn(ex.getMessage());
        return new ResponseError(HttpStatus.NOT_FOUND,404,ex.getMessage());
    }
}
