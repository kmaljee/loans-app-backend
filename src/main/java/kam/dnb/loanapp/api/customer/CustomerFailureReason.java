package kam.dnb.loanapp.api.customer;

import kam.dnb.loanapp.api.error.FailureReason;

public enum CustomerFailureReason implements FailureReason {

    USERNAME_ALREADY_EXISTS("USERNAME_NOT_AVAILABLE", "This username is unavailable");

    private final String code;
    private final String description;

    CustomerFailureReason(final String code, final String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
