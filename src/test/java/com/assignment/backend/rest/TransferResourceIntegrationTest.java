package com.assignment.backend.rest;

import com.assignment.backend.BankApplication;
import com.assignment.backend.rest.request.AccountRequest;
import com.assignment.backend.rest.request.CreateAccountRequest;
import com.assignment.backend.rest.request.TransferRequest;
import com.assignment.backend.rest.response.AccountInfoResponse;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.jupiter.api.*;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.util.List;

import static com.assignment.backend.TestFixture.*;
import static com.assignment.backend.util.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TransferResourceIntegrationTest {
    private HttpServer server;
    private WebTarget target;

    @BeforeAll
    void setUp() {
        // start the server
        server = BankApplication.startServer();
        // create the client
        Client c = ClientBuilder.newClient();
        target = c.target(BankApplication.BASE_URI);
    }

    @AfterEach
    void cleanupDB() {

    }

    @AfterAll
    void tearDown() {
        server.shutdownNow();
    }

    @Test
    @Order(1)
    void givenCreateAccount_whenCorrectRequest_thenResponseIsOkAndContainsResponse() {
        Response response = createAccount(DUMMY_ACCOUNT_NUMBER);
        assertEquals(Status.OK.getStatusCode(), response.getStatus(), "Http Response should be 200: ");
        assertEquals(MediaType.TEXT_PLAIN, response.getHeaderString(HttpHeaders.CONTENT_TYPE), "Http Content-Type should be: ");
        String content = response.readEntity(String.class);
        assertEquals(String.format(DEFAULT_CREATION_MSG_FMT, DUMMY_ACCOUNT_NUMBER), content, "Content of response is: ");
    }

    @Test
    @Order(2)
    void givenCreateAccount_withDuplicateAccount_thenResponseIsBadRequestAndContainsResponse() {
        Response response = createAccount(DUMMY_ACCOUNT_NUMBER);
        assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus(), "Http Response should be 400: ");
        assertEquals(String.format(DEFAULT_AEE_MSG_FMT, DUMMY_ACCOUNT_NUMBER), response.readEntity(String.class), "Content of response is: ");
    }

    @Test
    @Order(3)
    void givenWithdrawAmount_whenCorrectRequest_thenResponseIsOkAndContainsResponse() {
        AccountRequest accountRequest = createAccountRequest(DUMMY_ACCOUNT_NUMBER, DUMMY_BALANCE);
        Response response = target.path("/account/withdraw").request()
                .post(Entity.entity(accountRequest, MediaType.APPLICATION_JSON));
        assertEquals(Status.OK.getStatusCode(), response.getStatus(), "Http Response should be 200: ");
        assertEquals(MediaType.TEXT_PLAIN, response.getHeaderString(HttpHeaders.CONTENT_TYPE), "Http Content-Type should be: ");
        assertEquals(String.format(DEFAULT_WITHDRAW_MSG_FMT, DUMMY_BALANCE, DUMMY_ACCOUNT_NUMBER, 0d), response.readEntity(String.class), "Content of response is: ");
    }

    @Test
    @Order(4)
    void givenWithdrawAmount_whenInsufficientBalance_thenResponseIsBadRequestAndContainsResponse() {
        AccountRequest accountRequest = createAccountRequest(DUMMY_ACCOUNT_NUMBER, DUMMY_BALANCE);
        Response response = target.path("/account/withdraw").request()
                .post(Entity.entity(accountRequest, MediaType.APPLICATION_JSON));
        assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus(), "Http Response should be 400: ");
        assertEquals(MediaType.TEXT_PLAIN, response.getHeaderString(HttpHeaders.CONTENT_TYPE), "Http Content-Type should be: ");
        assertEquals(String.format(DEFAULT_ISBE_MSG_FMT, DUMMY_ACCOUNT_NUMBER), response.readEntity(String.class), "Content of response is: ");
    }

    @Test
    @Order(5)
    void givenWithdrawAmount_whenInvalidAmount_Throws_ProcessException() {
        AccountRequest accountRequest = createAccountRequest(DUMMY_ACCOUNT_NUMBER, 0d);
        assertThrows(ProcessingException.class, () -> target.path("/account/withdraw").request()
                .post(Entity.entity(accountRequest, MediaType.APPLICATION_JSON)));
    }

    @Test
    @Order(6)
    void givenDepositAmount_whenCorrectRequest_thenResponseIsOkAndContainsResponse() {
        AccountRequest accountRequest = createAccountRequest(DUMMY_ACCOUNT_NUMBER, DUMMY_BALANCE);
        Response response = target.path("/account/deposit").request()
                .post(Entity.entity(accountRequest, MediaType.APPLICATION_JSON));
        assertEquals(Status.OK.getStatusCode(), response.getStatus(), "Http Response should be 200: ");
        assertEquals(MediaType.TEXT_PLAIN, response.getHeaderString(HttpHeaders.CONTENT_TYPE), "Http Content-Type should be: ");
        assertEquals(String.format(DEFAULT_DEPOSIT_MSG_FMT, DUMMY_BALANCE, DUMMY_ACCOUNT_NUMBER, 10d), response.readEntity(String.class), "Content of response is: ");
    }


    @Test
    @Order(7)
    void givenDepositAmount_whenInvalidAmount_Throws_ProcessException() {
        AccountRequest accountRequest = createAccountRequest(DUMMY_ACCOUNT_NUMBER, 0d);
        assertThrows(ProcessingException.class, () -> target.path("/account/deposit").request()
                .post(Entity.entity(accountRequest, MediaType.APPLICATION_JSON)));
    }

    @Test
    @Order(8)
    void givenTransfer_whenCorrectRequest_thenResponseIsOkAndContainsResponse() {
        Response response = createAccount(DUMMY_ACCOUNT_NUMBER_1);
        assertEquals(String.format(DEFAULT_CREATION_MSG_FMT, DUMMY_ACCOUNT_NUMBER_1), response.readEntity(String.class), "Content of response is: ");
        TransferRequest transferRequest = createTransferRequest(DUMMY_ACCOUNT_NUMBER, DUMMY_ACCOUNT_NUMBER_1, 10d);

        Response transferResponse = target.path("/account/transfer").request()
                .post(Entity.entity(transferRequest, MediaType.APPLICATION_JSON));
        assertEquals(Status.OK.getStatusCode(), transferResponse.getStatus(), "Http Response should be 200: ");
        assertEquals(MediaType.TEXT_PLAIN, transferResponse.getHeaderString(HttpHeaders.CONTENT_TYPE), "Http Content-Type should be: ");
        assertEquals(String.format(DEFAULT_TRANSFER_MSG_FMT, transferRequest.getAmount(), transferRequest.getToAccount(), transferRequest.getFromAccount()), transferResponse.readEntity(String.class), "Content of response is: ");
    }

    @Test
    @Order(9)
    void givenTransfer_whenInvalidAmount_Throws_ProcessException() {

        TransferRequest transferRequest = createTransferRequest(DUMMY_ACCOUNT_NUMBER, DUMMY_ACCOUNT_NUMBER_1, 0d);
        assertThrows(ProcessingException.class, () -> target.path("/account/transfer").request()
                .post(Entity.entity(transferRequest, MediaType.APPLICATION_JSON)));
    }

    @Test
    @Order(10)
    void givenTransfer_withInsufficient_Amount__Throws_ProcessException() {
        TransferRequest transferRequest = createTransferRequest(DUMMY_ACCOUNT_NUMBER, DUMMY_ACCOUNT_NUMBER_1, 10d);
        Response transferResponse = target.path("/account/transfer").request()
                .post(Entity.entity(transferRequest, MediaType.APPLICATION_JSON));
        assertEquals(Status.BAD_REQUEST.getStatusCode(), transferResponse.getStatus(), "Http Response should be 400: ");
        assertEquals(MediaType.TEXT_PLAIN, transferResponse.getHeaderString(HttpHeaders.CONTENT_TYPE), "Http Content-Type should be: ");
        assertEquals(String.format(DEFAULT_ISBE_MSG_FMT, transferRequest.getFromAccount()), transferResponse.readEntity(String.class), "Content of response is: ");
    }

    @Test
    @Order(11)
    void givenGetList_thenResponseIsOkAndContainsResponse() {
        List<AccountInfoResponse> response = target.path("/account/list").request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<AccountInfoResponse>>(){});
        assertEquals(2, response.size());
    }

    private Response createAccount(String dummyAccountNumber) {
        CreateAccountRequest accountRequest = getCreateAccountRequest(dummyAccountNumber, DUMMY_BALANCE, DUMMY_FIRST_NAME, DUMMY_LAST_NAME);

        return target.path("/account/create").request()
                .post(Entity.entity(accountRequest, MediaType.APPLICATION_JSON));
    }


}