package com.rbs.transfer.resource;

import com.rbs.transfer.domain.Account;
import com.rbs.transfer.dto.AccountDTO;
import com.rbs.transfer.dto.TransferDTO;
import com.rbs.transfer.exception.AccountException;
import com.rbs.transfer.exception.TransferException;
import com.rbs.transfer.service.AccountServiceImpl;
import com.rbs.transfer.service.TransferServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {
	@InjectMocks
	private AccountController accountController;

	@Mock
	private AccountServiceImpl accountService;

	@Spy
	private TransferServiceImpl transferService;

	@Test
	public void shouldCreateAccountSuccessfully() {
		Account account = Account.builder().name("1234").balance(BigDecimal.TEN).build();
		AccountDTO accountDTO = AccountDTO.builder().name("1234").balance(BigDecimal.TEN).build();
		when(accountService.createAccount("1234", BigDecimal.TEN)).thenReturn(account);
		assertThat(accountController.createAccount(accountDTO).getStatusCode()).isEqualTo(HttpStatus.CREATED);
	}

	@Test
	public void shouldReturnBadRequestWhenCreateAccount() {
		Account account = Account.builder().name("1234").balance(BigDecimal.TEN).build();
		AccountDTO accountDTO = AccountDTO.builder().name("1234").balance(BigDecimal.TEN).build();
		when(accountService.createAccount("1234", BigDecimal.TEN)).thenThrow(new AccountException("Exception happened"));
		ResponseEntity<String> responseEntity = accountController.createAccount(accountDTO);
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).isEqualTo("Exception happened");
	}

	@Test
	public void shouldSuccessfullyGetAccountInformation() {
		Account account = Account.builder().name("1234").balance(BigDecimal.TEN).build();
		when(accountService.getAccount(any())).thenReturn(account);
		ResponseEntity responseEntity = accountController.getAccount("1234");
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void shouldReturnBadRequestWhenGettingAccountInfo() {
		when(accountService.getAccount(any())).thenThrow(new AccountException("Exception happened"));
		ResponseEntity responseEntity = accountController.getAccount("1234");
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).isEqualTo("Exception happened");
	}

	@Test
	public void shouldSuccessfullyTransferMoney() {
		TransferDTO transferDTO = TransferDTO.builder().to("123").to("234").amount(BigDecimal.ONE).build();
		doNothing().when(transferService).transfer(any(), any(), any());
		ResponseEntity responseEntity = accountController.transfer(transferDTO);
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	public void shouldReturnBadRequestWhenTransferMoney() {
		TransferDTO transferDTO = TransferDTO.builder().to("123").to("234").amount(BigDecimal.ONE).build();
		doThrow(new TransferException("Exception happened")).when(transferService).transfer(any(), any(), any());
		ResponseEntity responseEntity = accountController.transfer(transferDTO);
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody()).isEqualTo("Exception happened");
	}

}
