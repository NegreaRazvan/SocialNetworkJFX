package layers.domain.validators;

public class UsernameUpperCaseException extends RuntimeException {
    public UsernameUpperCaseException() {
    }

    public UsernameUpperCaseException(String message) {
      super(message);
    }

    public UsernameUpperCaseException(String message, Throwable cause) {
      super(message, cause);
    }

    public UsernameUpperCaseException(Throwable cause) {
      super(cause);
    }

    public UsernameUpperCaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
      super(message, cause, enableSuppression, writableStackTrace);
    }
}
