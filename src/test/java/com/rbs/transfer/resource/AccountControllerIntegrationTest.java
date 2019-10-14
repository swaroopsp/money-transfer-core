package com.rbs.transfer.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbs.transfer.Application;
import com.rbs.transfer.domain.Account;
import com.rbs.transfer.dto.TransferDTO;
import com.rbs.transfer.service.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = Application.class)
public class AccountControllerIntegrationTest {
	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Autowired
	private AccountService accountService;

	@Mock
	private AccountService mockAccountService;

	@Before
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void shouldCreateAccountSuccessfully() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
				.post("/account")
				.content(asJsonString(Account.builder().name("11111").balance(BigDecimal.TEN).build()))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
		Account account = accountService.getAccount("11111");

		assertThat(account.getName()).isEqualTo("11111");
		assertThat(account.getBalance()).isEqualTo(BigDecimal.TEN);
	}

	@Test
	public void shouldReturnBadRequestWhenCreateAccount() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
				.post("/account")
				.content(asJsonString(Account.builder().name("").balance(BigDecimal.TEN).build()))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(content().string("Account must have a name"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void shouldGetTheAccountSuccessfully() throws Exception {
		accountService.createAccount("22222", BigDecimal.TEN);
		mockMvc.perform(MockMvcRequestBuilders
				.get("/account/22222")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("22222"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(10));
	}

	@Test
	public void shouldReturnBadRequestWhenGetTheNotExistingAccount() throws Exception {
		accountService.createAccount("22223", BigDecimal.TEN);
		mockMvc.perform(MockMvcRequestBuilders
				.get("/account/22224")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(content().string("Unknown account: 22224"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void shouldTransferMoneyBetweenAccountsSuccessfully() throws Exception {
		accountService.createAccount("33333", BigDecimal.TEN);
		accountService.createAccount("44444", BigDecimal.ONE);
		mockMvc.perform(MockMvcRequestBuilders
				.put("/account/transfer")
				.content(asJsonString(TransferDTO.builder().from("33333").to("44444").amount(BigDecimal.ONE).build()))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		assertThat(accountService.getAccount("33333").getBalance()).isEqualTo(new BigDecimal("9"));
		assertThat(accountService.getAccount("44444").getBalance()).isEqualTo(new BigDecimal("2"));
	}

	@Test
	public void shouldReturnBadRequestWhenTransferMoneyBetweenAccountsWithInvalidSource() throws Exception {
		accountService.createAccount("55555", BigDecimal.TEN);
		accountService.createAccount("66666", BigDecimal.ONE);
		mockMvc.perform(MockMvcRequestBuilders
				.put("/account/transfer")
				.content(asJsonString(TransferDTO.builder().from("").to("66666").amount(BigDecimal.ONE).build()))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(content().string("Source account is not valid"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void shouldReturnBadRequestWhenTransferMoneyBetweenAccountsWithInvalidDestination() throws Exception {
		accountService.createAccount("77777", BigDecimal.TEN);
		accountService.createAccount("88888", BigDecimal.ONE);
		mockMvc.perform(MockMvcRequestBuilders
				.put("/account/transfer")
				.content(asJsonString(TransferDTO.builder().from("77777").to("").amount(BigDecimal.ONE).build()))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(content().string("Target account is not valid"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void shouldReturnBadRequestWhenTransferMoneyBetweenAccountsWithInvalidAmount() throws Exception {
		accountService.createAccount("99998", BigDecimal.TEN);
		accountService.createAccount("99999", BigDecimal.ONE);
		mockMvc.perform(MockMvcRequestBuilders
				.put("/account/transfer")
				.content(asJsonString(TransferDTO.builder().from("99998").to("99999").amount(BigDecimal.ZERO).build()))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(content().string("Transaction from 99998 to 99999 must be greater than 0"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void shouldReturnBadRequestWhenTransferMoneyBetweenAccountsWithNegativeAmount() throws Exception {
		accountService.createAccount("99988", BigDecimal.TEN);
		accountService.createAccount("99989", BigDecimal.ONE);
		mockMvc.perform(MockMvcRequestBuilders
				.put("/account/transfer")
				.content(asJsonString(TransferDTO.builder().from("99988").to("99989").amount(new BigDecimal("-1")).build()))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(content().string("Transaction from 99988 to 99989 must be greater than 0"))
				.andExpect(status().isBadRequest());
	}

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
