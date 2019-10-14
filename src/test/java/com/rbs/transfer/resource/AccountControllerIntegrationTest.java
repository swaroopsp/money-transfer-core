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
    public void shouldCreateAccountSuccessfully() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders
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
    public void shouldGetTheAccountSuccessfully() throws Exception
    {
        accountService.createAccount("22222", BigDecimal.TEN);
        mockMvc.perform( MockMvcRequestBuilders
                .get("/account/22222")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("22222"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(10));
    }

    @Test
    public void shouldTransferMoneyBetweenAccountsSuccessfully() throws Exception
    {
        accountService.createAccount("33333", BigDecimal.TEN);
        accountService.createAccount("44444", BigDecimal.ONE);
        mockMvc.perform( MockMvcRequestBuilders
                .put("/account/transfer")
                .content(asJsonString(TransferDTO.builder().from("33333").to("44444").amount(BigDecimal.ONE).build()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertThat(accountService.getAccount("33333").getBalance()).isEqualTo(new BigDecimal("9"));
        assertThat(accountService.getAccount("44444").getBalance()).isEqualTo(new BigDecimal("2"));
    }

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
