package kam.dnb.loanapp.api.loanapplication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {

    /**
     * @param username The username of the customer whose loan applications this method should return
     * @return All loan applications belonging to the specified user or else an empty list
     */
    List<LoanApplication> findByCustomer_User_Username(final String username);

}