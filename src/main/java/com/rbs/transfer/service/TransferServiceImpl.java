package com.rbs.transfer.service;

import java.math.BigDecimal;

import com.rbs.transfer.exception.AccountException;
import com.rbs.transfer.exception.TransferException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rbs.transfer.domain.Account;
import com.rbs.transfer.domain.Transfer;
import com.rbs.transfer.repository.TransferRepository;

@Service
public class TransferServiceImpl implements TransferService {

	@Autowired
	private AccountService accountService;

	@Autowired
	private TransferRepository transferRepository;

	public void transfer(String source, String target, BigDecimal amount) throws TransferException {
		if (amount == null || BigDecimal.ZERO.compareTo(amount) >= 0) {
			throw new TransferException("Transaction from " + source + " to " + target + " must be greater than 0");
		}

		try {
			Account sourceAccount = accountService.getAccount(source);
			Account targetAccount = accountService.getAccount(target);

			registerTransfer(source, target, amount);

			accountService.authorize(sourceAccount, amount);

			try {
				accountService.credit(targetAccount, amount);
			} catch (AccountException e) {
				// refund original account
				accountService.credit(sourceAccount, amount.negate());
				throw e;
			}
		} catch (RuntimeException e) {
			new TransferException(e.getMessage());
		}
	}

	private void registerTransfer(String fromAccount, String toAccount, BigDecimal amount) {
		Transfer transfer = new Transfer();
		transfer.setOrigin(fromAccount);
		transfer.setTarget(toAccount);
		transfer.setAmount(amount);
		transferRepository.add(transfer);
	}
}
