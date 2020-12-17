package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.ErrorInfo;
import ru.javawebinar.topjava.util.exception.ErrorType;
import ru.javawebinar.topjava.util.exception.IllegalRequestDataException;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;

import java.util.*;

import static ru.javawebinar.topjava.util.exception.ErrorType.*;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {
    @Autowired
    private MessageSource messageSource;
    private static Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);
    public static final String EXCEPTION_USER_DUPLICATE_MAIL = "exception.user.duplicateMail";
    public static final String EXCEPTION_USER_DUPLICATE_DATETIME = "exception.meal.duplicateDateTime";


    //  http://stackoverflow.com/a/22358422/548473
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo handleError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false, DATA_NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        Map<String, String> exceptions = Map.of("users_unique_email_idx", EXCEPTION_USER_DUPLICATE_MAIL,
                "meals_unique_user_datetime_idx", EXCEPTION_USER_DUPLICATE_DATETIME);
        for (String key : exceptions.keySet()) {
            if (Objects.requireNonNull(e.getMessage()).contains(key)) {
                String msg = messageSource.getMessage(exceptions.get(key), null, Locale.getDefault());
                return logAndGetErrorInfo(req, new DataIntegrityViolationException(msg), false, DATA_ERROR);
            }
        }
        return logAndGetErrorInfo(req, e, true, DATA_ERROR);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)  // 422
    @ExceptionHandler({IllegalRequestDataException.class, MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ErrorInfo illegalRequestDataError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({BindException.class})
    public ErrorInfo validationError(HttpServletRequest req, BindException e) {
        List<String> errorDetails = ValidationUtil.getErrorDetails(e.getBindingResult());
        return logAndGetErrorInfo(req, new ValidationException(""), false, VALIDATION_ERROR, errorDetails.toArray(new String[0]));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, APP_ERROR);
    }

    //    https://stackoverflow.com/questions/538870/should-private-helper-methods-be-static-if-they-can-be-static
    private static ErrorInfo logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logException, ErrorType errorType, String... errorDetails) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        errorDetails = errorDetails.length == 0 ? new String[]{rootCause.getMessage()} : errorDetails;
        if (logException) {
            log.error(errorType + " at request " + req.getRequestURL(), rootCause);
        } else {
            log.warn("{} at request  {}: {}", errorType, req.getRequestURL(), errorDetails);
        }
        return new ErrorInfo(req.getRequestURL(), errorType, errorDetails);
    }
}