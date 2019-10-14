package com.rbs.transfer.repository;

import com.rbs.transfer.domain.Account;

public interface AccountRepository {
	Account get(String name);
	Account create(Account account);
}
