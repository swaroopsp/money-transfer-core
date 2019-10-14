package com.rbs.transfer.service;

import com.rbs.transfer.Application;
import com.rbs.transfer.domain.Account;
import com.rbs.transfer.exception.TransferException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TransferServiceTest {
	@Autowired
	private AccountService accountService;

	@Mock
	private AccountService mockAccountService;

	@Autowired
	private TransferService transferService;

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Test
	public void shouldThrowThrowExceptionWhenCreditWithValidAccountButWithNullAmount() {
		exception.expect(TransferException.class);
		exception.expectMessage("Transaction from 1111 to 2222 must be greater than 0");
		transferService.transfer("1111", "2222",  null);
	}

	@Test
	public void shouldThrowThrowExceptionWhenCreditWithValidAccountButWithZeroAmount() {
		exception.expect(TransferException.class);
		exception.expectMessage("Transaction from 1111 to 2222 must be greater than 0");
		transferService.transfer("1111", "2222",  BigDecimal.ZERO);
	}


	@Test
	public void shouldTransferMoneyFromSourceToDestinationAccount() {
		Account accountSource = accountService.createAccount("3333", BigDecimal.TEN);
		assertThat(accountSource).isNotNull();
		assertThat(accountSource.getBalance()).isEqualTo(BigDecimal.TEN);


		Account accountDestination = accountService.createAccount("4444", BigDecimal.ONE);
		assertThat(accountDestination).isNotNull();
		assertThat(accountDestination.getBalance()).isEqualTo(BigDecimal.ONE);

		transferService.transfer("3333", "4444",  BigDecimal.ONE);

		assertThat(accountService.getAccount("3333").getBalance()).isEqualTo(new BigDecimal("9"));
		assertThat(accountService.getAccount("4444").getBalance()).isEqualTo(new BigDecimal("2"));
	}
}
