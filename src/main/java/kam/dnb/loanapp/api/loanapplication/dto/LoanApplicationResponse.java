package kam.dnb.loanapp.api.loanapplication.dto;

import kam.dnb.loanapp.api.loanapplication.LoanApplication;
import kam.dnb.loanapp.api.loanapplication.LoanApplicationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LoanApplicationResponse(Long id,
                                      Long customerId,
                                      LocalDateTime created,
                                      LocalDateTime updated,
                                      BigDecimal loanAmount,
                                      BigDecimal equityAmount,
                                      BigDecimal salary,
                                      LoanApplicationStatus status) {

    public LoanApplicationResponse(final LoanApplication loanApplication) {
        this(
                loanApplication.getId(),
                loanApplication.getCustomer().getId(),
                loanApplication.getCreated(),
                loanApplication.getUpdated(),
                loanApplication.getLoanAmount(),
                loanApplication.getEquityAmount(),
                loanApplication.getSalary(),
                loanApplication.getStatus()
        );
    }
}
