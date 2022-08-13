package advanced.com.demo.springboot.backend.exception;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler  {

    HttpStatus badRequest = HttpStatus.BAD_REQUEST;
    HttpStatus notFound = HttpStatus.NOT_FOUND;
    HttpStatus methodNotAllowed = HttpStatus.METHOD_NOT_ALLOWED;
    HttpStatus unsupportedMediaType = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
    HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;

    ZonedDateTime timestamp = ZonedDateTime.now(ZoneId.of("Asia/Kathmandu"));

    // 400

    @ExceptionHandler(value = {CustomApiException.class})
    protected ResponseEntity<Object> handleBadRequest(CustomApiException ex) {
        logger.info(ex.getClass().getName());

        final List<String> errors = new ArrayList<String>();
        errors.add("Errors Occurred");

        final ApiError apiError = new ApiError(
                ex.getLocalizedMessage(),
                badRequest,
                timestamp,
                errors
        );
        return new ResponseEntity<>(apiError, badRequest);
    }

    // 404

    @ExceptionHandler(value = {CustomNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(CustomNotFoundException ex) {
        logger.info(ex.getClass().getName());

        final List<String> errors = new ArrayList<String>();
        errors.add("Errors Occurred");

        final ApiError apiError = new ApiError(
                ex.getLocalizedMessage(),
                notFound,
                timestamp,
                errors
        );
        return new ResponseEntity<>(apiError, notFound);
    }

    // 400

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                 final HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final List<String> errors = new ArrayList<String>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        final ApiError apiError = new ApiError(ex.getLocalizedMessage(),badRequest, timestamp, errors);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleBindException(final BindException ex,
                                                         final HttpHeaders headers,
                                                         final HttpStatus status,
                                                         final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final List<String> errors = new ArrayList<String>();

        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        final ApiError apiError = new ApiError(ex.getLocalizedMessage(),badRequest, timestamp, errors);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final List<String> errors = new ArrayList<String>();
        errors.add(ex.getValue() + " value for " + ex.getPropertyName() + " should be of type " + ex.getRequiredType());

        final ApiError apiError = new ApiError(ex.getLocalizedMessage(),badRequest,timestamp, errors);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final List<String> errors = new ArrayList<String>();
        errors.add(ex.getRequestPartName() + " part is missing");
        final ApiError apiError = new ApiError(ex.getLocalizedMessage(),badRequest,timestamp, errors);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final List<String> errors = new ArrayList<String>();
        errors.add(ex.getParameterName() + " parameter is missing");
        final ApiError apiError = new ApiError(ex.getLocalizedMessage(),badRequest,timestamp, errors);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getHttpStatus());
    }

    //

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final List<String> errors = new ArrayList<String>();
        errors.add(ex.getName() + " should be of type " + ex.getRequiredType().getName());

        final ApiError apiError = new ApiError(ex.getLocalizedMessage(),badRequest,timestamp, errors);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getHttpStatus());
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final List<String> errors = new ArrayList<String>();
        for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": " + violation.getMessage());
        }

        final ApiError apiError = new ApiError(ex.getLocalizedMessage(),badRequest,timestamp, errors);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getHttpStatus());
    }

    // 404

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final List<String> errors = new ArrayList<String>();
        errors.add("No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL());

        final ApiError apiError = new ApiError(ex.getLocalizedMessage(),notFound,timestamp, errors);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getHttpStatus());
    }

    // 405

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final List<String> errors = new ArrayList<String>();

        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));
        errors.add(builder.toString());

        final ApiError apiError = new ApiError(ex.getLocalizedMessage(),methodNotAllowed,timestamp, errors);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getHttpStatus());
    }

    // 415

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final List<String> errors = new ArrayList<String>();
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t + " "));
        errors.add(builder.substring(0, builder.length() - 2));

        final ApiError apiError = new ApiError(ex.getLocalizedMessage(),unsupportedMediaType, timestamp, errors );
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getHttpStatus());
    }

    // 500

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
        logger.info(ex.getClass().getName());
        logger.error("error", ex);
        //
        final List<String> errors = new ArrayList<String>();
        errors.add("Errors Occurred");

        final ApiError apiError = new ApiError(ex.getLocalizedMessage(),internalServerError, timestamp,  errors);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getHttpStatus());
    }

}
