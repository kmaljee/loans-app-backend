package kam.dnb.loanapp.api.loanapplication.dto;

import java.math.BigDecimal;

public record LoanApplicationRequest(BigDecimal loanAmount, BigDecimal equityAmount, BigDecimal salary) {

}
