package com.rbs.transfer.service;

import java.math.BigDecimal;

import com.rbs.transfer.domain.Account;
import com.rbs.transfer.exception.AccountException;

public interface AccountService {
	Account getAccount(String name) throws AccountException;
	Account createAccount(String name, BigDecimal initialBalance) throws AccountException;
	void authorize(Account account, BigDecimal amount) throws AccountException;
	void credit(Account account, BigDecimal amount) throws AccountException;
}
