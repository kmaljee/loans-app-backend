package kam.dnb.loanapp.api.error;

/**
 * The error response object returned when a request fails.
 */
public class ErrorResponse {

    private final String errorCode;
    private final String errorDescription;

    public ErrorResponse(String errorCode, String errorDescription) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    public ErrorResponse(final FailureReason reason) {
        this.errorCode = reason.getCode();
        this.errorDescription = reason.getDescription();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }
}
