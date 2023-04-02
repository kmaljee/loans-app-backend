package kam.dnb.loanapp.api.loanapplication;

import kam.dnb.loanapp.api.error.FailureReason;

public enum ApplicationFailureReason implements FailureReason {

    INVALID_CUSTOMER("INVALID_CUSTOMER", "Customer does not exist or user is not a customer"),
    INVALID_LOAN_AMOUNT("INVALID_LOAN_AMOUNT", "Loan amount must be greater than zero"),
    INVALID_EQUITY_AMOUNT("INVALID_EQUITY_AMOUNT", "Equity amount cannot be negative"),
    INVALID_SALARY("INVALID_SALARY", "Salary cannot be negative");

    private final String code;
    private final String description;

    ApplicationFailureReason(final String code, final String description) {
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
