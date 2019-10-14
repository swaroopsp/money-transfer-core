package com.rbs.transfer.service;

import java.math.BigDecimal;

import com.rbs.transfer.exception.AccountException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.rbs.transfer.domain.Account;
import com.rbs.transfer.repository.AccountRepository;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepository;

	public Account getAccount(String name) throws AccountException {
		if (StringUtils.isEmpty(name)) {
			throw new AccountException("Invalid name");
		}

		Account account = accountRepository.get(name);
		if (account == null) {
			throw new AccountException("Unknown account: " + name);
		}

		return account;
	}

	public Account createAccount(String name, BigDecimal initialBalance) throws AccountException {
		if (StringUtils.isEmpty(name)) {
			throw new AccountException("Account must have a name");
		}

		if (initialBalance == null || BigDecimal.ZERO.compareTo(initialBalance) >= 0) {
			throw new AccountException("Initial balance must be greater or equals to zero");
		}

		Account account = Account.builder().name(name).balance(initialBalance).build();

		Account created = accountRepository.create(account);
		if (created != null) {
			throw new AccountException("Account " + name + " already exists");
		}

		return account;
	}

	@Override
	public void authorize(Account account, BigDecimal amount) throws AccountException {
		if (account == null) {
			throw new AccountException("Undefined account");
		}

		if (amount == null || BigDecimal.ZERO.compareTo(amount) >= 0) {
			throw new AccountException("Amount must be greater than zero");
		}

		synchronized (account) {
			BigDecimal balance = account.getBalance();
			balance = balance.subtract(amount);
			if (BigDecimal.ZERO.compareTo(balance) <= 0) {
				account.setBalance(balance);
			} else {
				throw new AccountException("Amount " + amount + " not available for account " + account.getName());
			}
		}
	}

	@Override
	public void credit(Account account, BigDecimal amount) throws AccountException {
		if (account == null) {
			throw new AccountException("Undefined account");
		}

		if (amount == null || BigDecimal.ZERO.compareTo(amount) >= 0) {
			throw new AccountException("Amount must be greater than zero");
		}

		synchronized (account) {
			BigDecimal balance = account.getBalance();
			balance = balance.add(amount);
			account.setBalance(balance);
		}
	}
}
