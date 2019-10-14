package com.rbs.transfer.service;

import com.rbs.transfer.domain.Account;
import com.rbs.transfer.domain.Transfer;
import com.rbs.transfer.exception.AccountException;
import com.rbs.transfer.exception.TransferException;
import com.rbs.transfer.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransferServiceImpl implements TransferService {

	@Autowired
	private AccountService accountService;

	@Autowired
	private TransferRepository transferRepository;

	public void transfer(String source, String target, BigDecimal amount) throws TransferException {
		Optional.ofNullable(source)
				.filter(src -> !StringUtils.isEmpty(src))
				.orElseThrow(() -> new TransferException("Source account is not valid"));
		Optional.ofNullable(target)
				.filter(tgt -> !StringUtils.isEmpty(tgt))
				.orElseThrow(() -> new TransferException("Target account is not valid"));

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
