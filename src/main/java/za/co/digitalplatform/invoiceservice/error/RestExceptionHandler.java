package za.co.digitalplatform.invoiceservice.error;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private ErrorDetail buildErrorDetail(String title, String detail, Integer status) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(Calendar.getInstance().getTime());
        errorDetail.setStatus(status);
        errorDetail.setTitle(title);
        errorDetail.setDetail(detail);

        return errorDetail;
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        return new ResponseEntity<>(buildErrorDetail("Resource Not Found",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value()),
                null,
                HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorDetail errorDetail = buildErrorDetail("Validation Failed",
                "Input validation failed",
                HttpStatus.BAD_REQUEST.value());

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        for (FieldError fe : fieldErrors) {
            List<ValidationError> validationErrorList = errorDetail.getErrors().
                    get(fe.getField());
            if (validationErrorList == null) {
                validationErrorList = new ArrayList<>();
                errorDetail.getErrors().put(fe.getField(),
                        validationErrorList);
            }
            ValidationError validationError = new ValidationError();
            validationError.setCode(fe.getCode());
            validationError.setMessage(fe.getDefaultMessage());
            validationErrorList.add(validationError);
        }
        return handleExceptionInternal(ex, errorDetail, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(Calendar.getInstance().getTime());
        errorDetail.setStatus(status.value());
        errorDetail.setTitle("Message Not Readable");
        errorDetail.setDetail(ex.getMessage());


        return handleExceptionInternal(ex, buildErrorDetail("Message Not Readable",
                ex.getMessage(),
                status.value()),
                headers,
                status,
                request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, buildErrorDetail("Missing Path Variable",
                ex.getMessage(),
                status.value()),
                headers,
                status,
                request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, buildErrorDetail("Type Mismatch",
                ex.getMessage(),
                status.value()),
                headers,
                status,
                request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globleExceptionHandler(Exception ex, WebRequest request) {
        return new ResponseEntity<>(buildErrorDetail("Internal Server Error",
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
