package com.rbs.transfer.resource;

import com.rbs.transfer.domain.Account;
import com.rbs.transfer.dto.AccountDTO;
import com.rbs.transfer.dto.TransferDTO;
import com.rbs.transfer.service.AccountService;
import com.rbs.transfer.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private TransferService transferService;

	@RequestMapping(value = "/{name}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAccount(@PathVariable String name) {
		try {
			Account account = accountService.getAccount(name);
			AccountDTO dto = new AccountDTO();
			dto.setName(account.getName());
			dto.setBalance(account.getBalance());
			return new ResponseEntity<AccountDTO>(dto, HttpStatus.OK);
		} catch (RuntimeException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createAccount(@RequestBody AccountDTO account) {
		try {
			accountService.createAccount(account.getName(), account.getBalance());
			return new ResponseEntity<String>(HttpStatus.CREATED);
		} catch (RuntimeException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/transfer", method = RequestMethod.PUT, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<String> transfer(@RequestBody TransferDTO transfer) {
		try {
			transferService.transfer(transfer.getFrom(), transfer.getTo(), transfer.getAmount());
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (RuntimeException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
