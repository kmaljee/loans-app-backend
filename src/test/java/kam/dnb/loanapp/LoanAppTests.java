package kam.dnb.loanapp;

import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import kam.dnb.loanapp.api.auth.AuthResponse;
import kam.dnb.loanapp.api.customer.dto.CreateCustomerRequest;
import kam.dnb.loanapp.api.customer.dto.CustomerResponse;
import kam.dnb.loanapp.api.loanapplication.LoanApplicationStatus;
import kam.dnb.loanapp.api.loanapplication.dto.LoanApplicationRequest;
import kam.dnb.loanapp.api.loanapplication.dto.LoanApplicationResponse;
import kam.dnb.loanapp.api.user.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Some basic high level tests to demonstrate coverage of the requirements in the spec.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoanAppTests {

	@Autowired
	private WebTestClient webClient;

	// Typically we'd set up the test users in the test and reset between each but using pre-populated sample users for simplicity
	// I've also hard-coded many more values in these tests than I typically would due to time

	@Test
	void testCreatingNewCustomerAccount() {
		final String username = "testUser";
		final String email = "user@email.com";
		final String password = "password";
		final String fullName = "Test User";
		final String address = "Test user address";
		final String socialSecurity = "123-546-567";

		final CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest(username, email, password, password, fullName, address, socialSecurity);
		final CustomerResponse createCustomerResponse = webClient.post().uri("/api/customers").bodyValue(createCustomerRequest)
				.exchange()
				.expectStatus().isEqualTo(HTTPResponse.SC_OK)
				.expectBody(CustomerResponse.class)
				.returnResult()
				.getResponseBody();

		// Check that we get the correct customer creation response back
		assertThat(createCustomerResponse).isNotNull();
		assertThat(createCustomerResponse.username()).isEqualTo(username);
		assertThat(createCustomerResponse.fullName()).isEqualTo(fullName);
		assertThat(createCustomerResponse.address()).isEqualTo(address);
		assertThat(createCustomerResponse.email()).isEqualTo(email);
		assertThat(createCustomerResponse.socialSecurity()).isEqualTo(socialSecurity);
	}

	@Test
	void testCanSuccessfullyApplyForLoanAsCustomerUser() {
		final BigDecimal loanAmount = new BigDecimal(5000);
		final BigDecimal equityAmount = new BigDecimal(100);
		final BigDecimal salary = new BigDecimal(45000);
		final LocalDateTime registeredTime = LocalDateTime.now();

		final LoanApplicationRequest loanApplicationRequest = new LoanApplicationRequest(loanAmount, equityAmount, salary);

		final LoanApplicationResponse loanResponse = webClient.post().uri("/api/loans").bodyValue(loanApplicationRequest)
				.header(HttpHeaders.AUTHORIZATION, buildAuthorizationHeader("customer", "password"))
				.exchange()
				.expectStatus().isEqualTo(HTTPResponse.SC_OK)
				.expectBody(LoanApplicationResponse.class)
				.returnResult()
				.getResponseBody();

		// Check that we get the correct customer creation response back
		assertThat(loanResponse).isNotNull();
		assertThat(loanResponse.loanAmount()).isEqualTo(loanAmount);
		assertThat(loanResponse.equityAmount()).isEqualTo(equityAmount);
		assertThat(loanResponse.salary()).isEqualTo(salary);
		assertThat(loanResponse.status()).isEqualTo(LoanApplicationStatus.PENDING);
		assertThat(loanResponse.created()).isBetween(registeredTime, LocalDateTime.now());
		assertThat(loanResponse.updated()).isBetween(registeredTime, LocalDateTime.now());
	}

	@Test
	void testGetLoansEndpointCorrectlyOnlyReturnsCurrentCustomerLoans() {
		// Create a new customer user
		final String username = "testGetLoansCustomer";
		final String password = "password";

		final CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest(username, "unused", password, password, "unused", "unused", "unused");
		webClient.post().uri("/api/customers").bodyValue(createCustomerRequest)
				.exchange()
				.expectStatus().isEqualTo(HTTPResponse.SC_OK);


		// Create a new loan but as the general test customer user (not the newly created user)
		final LoanApplicationRequest loanApplicationRequest = new LoanApplicationRequest(new BigDecimal(100), new BigDecimal(200), new BigDecimal(10000));
		webClient.post().uri("/api/loans").bodyValue(loanApplicationRequest)
				.header(HttpHeaders.AUTHORIZATION, buildAuthorizationHeader("customer", "password"))
				.exchange()
				.expectStatus().isEqualTo(HTTPResponse.SC_OK);

		final ParameterizedTypeReference<List<LoanApplicationResponse>> typeRef = new ParameterizedTypeReference<>() {};
		// Check the newly created user does not see any loans when they call the /api/loans endpoint with get
		final List<LoanApplicationResponse> initialGetLoansResponse = webClient.get().uri("/api/loans")
				.header(HttpHeaders.AUTHORIZATION, buildAuthorizationHeader(username, password))
				.exchange()
				.expectStatus().isEqualTo(HTTPResponse.SC_OK)
				.expectBody(typeRef)
				.returnResult()
				.getResponseBody();

		assertThat(initialGetLoansResponse).isEmpty();

		// Create a loan using the new user and check that we can see that loan when calling the same endpoint
		final BigDecimal loanAmount = new BigDecimal(5000);
		final BigDecimal equityAmount = new BigDecimal(100);
		final BigDecimal salary = new BigDecimal(45000);
		final LocalDateTime registeredTime = LocalDateTime.now();

		final LoanApplicationRequest newLoanApplicationRequest = new LoanApplicationRequest(loanAmount, equityAmount, salary);
		webClient.post().uri("/api/loans").bodyValue(newLoanApplicationRequest)
				.header(HttpHeaders.AUTHORIZATION, buildAuthorizationHeader(username, password))
				.exchange()
				.expectStatus().isEqualTo(HTTPResponse.SC_OK);

		final List<LoanApplicationResponse> getLoansResponse = webClient.get().uri("/api/loans")
				.header(HttpHeaders.AUTHORIZATION, buildAuthorizationHeader(username, password))
				.exchange()
				.expectStatus().isEqualTo(HTTPResponse.SC_OK)
				.expectBody(typeRef)
				.returnResult()
				.getResponseBody();

		assertThat(getLoansResponse).hasSize(1);
		final LoanApplicationResponse createdLoan = getLoansResponse.get(0);
		assertThat(createdLoan.loanAmount()).isEqualByComparingTo(loanAmount);
		assertThat(createdLoan.equityAmount()).isEqualByComparingTo(equityAmount);
		assertThat(createdLoan.salary()).isEqualByComparingTo(salary);
		assertThat(createdLoan.status()).isEqualTo(LoanApplicationStatus.PENDING);
		assertThat(createdLoan.created()).isBetween(registeredTime, LocalDateTime.now());
		assertThat(createdLoan.updated()).isBetween(registeredTime, LocalDateTime.now());
	}

	@Test
	void testValidCustomerUserCannotCallGetAllLoansEndpoint() {
		webClient.get().uri("/api/loans/getAll")
				.header(HttpHeaders.AUTHORIZATION, buildAuthorizationHeader("customer", "password"))
				.exchange()
				.expectStatus().isEqualTo(HTTPResponse.SC_FORBIDDEN);
	}

	// Typically these would be done as unit tests and would be split out with many more test cases
	public static Stream<Arguments> parametersForTestingAuthentication() {
		return Stream.of(
				// Correct credentials
				Arguments.of("customer", "password", true, Role.CUSTOMER),
				// Incorrect username
				Arguments.of("customer_", "password", false, Role.CUSTOMER),
				// Incorrect password
				Arguments.of("customer", "password1", false, Role.CUSTOMER),
				// Incorrect password due to whitespace
				Arguments.of("customer", "password ", false, Role.CUSTOMER),

				// Perform same checks for adviser user
				// Correct credentials
				Arguments.of("adviser", "password", true, Role.ADVISER),
				// Incorrect username
				Arguments.of("adviser_", "password", false, Role.ADVISER),
				// Incorrect password
				Arguments.of("adviser", "password1", false, Role.ADVISER),
				// Incorrect password due to whitespace
				Arguments.of("adviser", "password ", false, Role.ADVISER)
		);
	}

	@ParameterizedTest
	@MethodSource("parametersForTestingAuthentication")
	void testAuthenticatingEndpoint(final String username, final String password, final boolean shouldAuthenticateSuccessfully, final Role expectedRole) {
		// Attempt to authenticate with the test customer account
		final WebTestClient.ResponseSpec response = webClient.post().uri("/api/auth")
				.header(HttpHeaders.AUTHORIZATION, buildAuthorizationHeader(username, password))
				.exchange();

		if (shouldAuthenticateSuccessfully) {
			final AuthResponse authenticationResponse = response
					.expectStatus().isEqualTo(HTTPResponse.SC_OK)
					.expectBody(AuthResponse.class)
					.returnResult()
					.getResponseBody();

			// Check that the authentication response contains the expected response
			assertThat(authenticationResponse).isNotNull();
			assertThat(authenticationResponse.getRole()).isEqualTo(expectedRole.name());
			assertThat(authenticationResponse.getToken()).isNotBlank();
		} else {
			// Invalid credentials so check we get unauthorized response
			response.expectStatus().isEqualTo(HTTPResponse.SC_UNAUTHORIZED);
		}
	}

	public static Stream<Arguments> parametersForUnauthenticatedRequests() {
		final Function<WebTestClient, WebTestClient.RequestHeadersUriSpec<?>> getRequest = WebTestClient::get;
		final Function<WebTestClient, WebTestClient.RequestHeadersUriSpec<?>> postRequest = WebTestClient::post;
		return Stream.of(
				Arguments.of(getRequest, "/api/loans"),
				Arguments.of(postRequest, "/api/loans"),
				Arguments.of(getRequest, "/api/customers/1"),
				Arguments.of(getRequest, "/api/customers/getAll")
		);
	}

	@ParameterizedTest
	@MethodSource("parametersForUnauthenticatedRequests")
	void testAuthorizationFailureAccessingProtectedEndpoints(final Function<WebTestClient, WebTestClient.RequestHeadersUriSpec<?>> requestType, final String endpoint) {
		requestType.apply(webClient).uri(endpoint)
				.header(HttpHeaders.AUTHORIZATION, buildAuthorizationHeader("invalid", "invalid"))
				.exchange()
				.expectStatus().isEqualTo(HTTPResponse.SC_UNAUTHORIZED);
	}


	private String buildAuthorizationHeader(final String username, final String password) {
		final String credentials = String.format("%s:%s", username, password);
		final String credentialsEncoded = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
		return String.format("Basic %s", credentialsEncoded);
	}

	// Additional basic use cases:
	// testCreatingCustomerWithDuplicateUsernameFails
	// testAdviserUserCanGetAllLoanApplication
	// testAdviserCanViewCustomerDetails
}
