package com.rbs.transfer.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.rbs.transfer.domain.Account;

@Service
public class InMemoryAccountRepository implements AccountRepository {

	private final Map<String, Account> accounts = new ConcurrentHashMap<>();

	@Override
	public Account get(String name) {
		return accounts.get(name);
	}

	@Override
	public Account create(Account account) {		
		return accounts.putIfAbsent(account.getName(), account);
	}

}
