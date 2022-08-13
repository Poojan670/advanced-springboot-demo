package advanced.com.demo.springboot.backend.exception;

public class CustomApiException extends RuntimeException{

    public CustomApiException(String message) {
        super(message);
    }

    public CustomApiException(String message, Throwable cause) {
        super(message, cause);
    }

}
