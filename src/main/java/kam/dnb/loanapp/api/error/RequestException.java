package kam.dnb.loanapp.api.error;

/**
 * Can be thrown by any request to return a response containing the failure code and description
 * with an HTTP response code of 400.
 */
public class RequestException extends RuntimeException {

    private final FailureReason failureReason;

    public RequestException(final FailureReason failureReason) {
        this.failureReason = failureReason;
    }

    public FailureReason getFailureReason() {
        return failureReason;
    }
}
