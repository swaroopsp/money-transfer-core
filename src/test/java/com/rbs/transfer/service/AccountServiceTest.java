package com.rbs.transfer.service;

import com.rbs.transfer.Application;
import com.rbs.transfer.domain.Account;
import com.rbs.transfer.exception.AccountException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AccountServiceTest {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Autowired
	private AccountService accountService;

	@Test
	public void shouldThrowAccountExceptionWhenAccountIsCreatedWithEmptyName(){
		exception.expect(AccountException.class);
		exception.expectMessage("Account must have a name");
		accountService.createAccount(null, null);
	}

	@Test
	public void shouldThrowAccountExceptionWhenAccountIsCreatedWithEmptyInitialBalance(){
		exception.expect(AccountException.class);
		exception.expectMessage("Initial balance must be greater or equals to zero");
		accountService.createAccount("123", null);
	}

	@Test
	public void shouldThrowAccountExceptionWhenAccountIsCreatedWithZeroInitialBalance(){
		exception.expect(AccountException.class);
		exception.expectMessage("Initial balance must be greater or equals to zero");
		accountService.createAccount("123", BigDecimal.ZERO);
	}

	@Test
	public void createAccountWithValidAccountNameAndInitialAmount() throws AccountException {
		Account account = accountService.createAccount("111", BigDecimal.TEN);
		assertThat(account).isNotNull();
		assertThat(account.getBalance()).isEqualTo(BigDecimal.TEN);
	}

	@Test
	public void throwThrowExceptionWhenAccountCreatedWithWithExistingAccountName() throws AccountException {
		Account account = accountService.createAccount("222", BigDecimal.TEN);
		assertThat(account).isNotNull();
		assertThat(account.getBalance()).isEqualTo(BigDecimal.TEN);

		exception.expect(AccountException.class);
		exception.expectMessage("Account 222 already exists");
		accountService.createAccount("222", BigDecimal.TEN);
	}

	@Test
	public void shouldThrowAccountExceptionWithEmptyAccountName() {
		exception.expect(AccountException.class);
		exception.expectMessage("Invalid name");
		accountService.getAccount(null);
	}

	@Test
	public void shouldThrowAccountExceptionWithUnknownAccountName() {
		exception.expect(AccountException.class);
		exception.expectMessage("Unknown account: 333");
		Account account = accountService.getAccount("333");
	}

	@Test
	public void shouldGetAccountTestWithValidAccountName() {
		Account account = accountService.createAccount("444", BigDecimal.TEN);
		assertThat(account).isNotNull();
		assertThat(account.getBalance()).isEqualTo(BigDecimal.TEN);

		Account createdAccount = accountService.getAccount("444");
		assertThat(createdAccount).isNotNull();
		assertThat(createdAccount.getName()).isEqualTo("444");
		assertThat(createdAccount.getBalance()).isEqualTo(BigDecimal.TEN);
	}

	@Test
	public void shouldThrowThrowExceptionWhenAuthoriseWithNullAccount() {
		exception.expect(AccountException.class);
		exception.expectMessage("Undefined account");
		Account account = null;
		accountService.authorize(account, BigDecimal.TEN);
	}

	@Test
	public void shouldThrowThrowExceptionWhenAuthoriseWithValidAccountButWithNullAmount() {

		Account account = accountService.createAccount("555", BigDecimal.TEN);
		assertThat(account).isNotNull();
		assertThat(account.getBalance()).isEqualTo(BigDecimal.TEN);

		exception.expect(AccountException.class);
		exception.expectMessage("Amount must be greater than zero");
		accountService.authorize(account, BigDecimal.ZERO);
	}

	@Test
	public void shouldThrowThrowExceptionWhenAuthoriseWithAmountGreaterThanTheAvailableAmount() {
		Account account = accountService.createAccount("666", BigDecimal.ONE);
		assertThat(account).isNotNull();
		assertThat(account.getBalance()).isEqualTo(BigDecimal.ONE);

		exception.expect(AccountException.class);
		exception.expectMessage("Amount 10 not available for account 666");
		accountService.authorize(account, BigDecimal.TEN);
	}

	@Test
	public void shouldAuthoriseWithValidAccountAndValidAmount() {

		Account account = accountService.createAccount("777", BigDecimal.TEN);
		assertThat(account).isNotNull();
		assertThat(account.getBalance()).isEqualTo(BigDecimal.TEN);

	    accountService.authorize(account, BigDecimal.ONE);
	    assertThat(account.getBalance()).isEqualTo(new BigDecimal("9"));
	}

	@Test
	public void shouldThrowThrowExceptionWhenCreditWithNullAccount() {
		exception.expect(AccountException.class);
		exception.expectMessage("Undefined account");
		Account account = null;
		accountService.credit(account, BigDecimal.TEN);
	}

	@Test
	public void shouldThrowThrowExceptionWhenCreditWithValidAccountButWithNullAmount() {
		Account account = accountService.createAccount("888", BigDecimal.TEN);
		assertThat(account).isNotNull();
		assertThat(account.getBalance()).isEqualTo(BigDecimal.TEN);

		exception.expect(AccountException.class);
		exception.expectMessage("Amount must be greater than zero");
		accountService.authorize(account, null);
	}

	@Test
	public void shouldThrowThrowExceptionWhenCreditWithValidAccountButWithZeroAmount() {
		Account account = accountService.createAccount("999", BigDecimal.TEN);
		assertThat(account).isNotNull();
		assertThat(account.getBalance()).isEqualTo(BigDecimal.TEN);

		exception.expect(AccountException.class);
		exception.expectMessage("Amount must be greater than zero");
		accountService.authorize(account, BigDecimal.ZERO);
	}

	@Test
	public void shouldCreditWithValidAccountAndValidAmount() {

		Account account = accountService.createAccount("1000", BigDecimal.TEN);
		assertThat(account).isNotNull();
		assertThat(account.getBalance()).isEqualTo(BigDecimal.TEN);

		accountService.credit(account, BigDecimal.ONE);
		assertThat(account.getBalance()).isEqualTo(new BigDecimal("11"));
	}
}
