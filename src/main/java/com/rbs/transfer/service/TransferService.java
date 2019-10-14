package com.rbs.transfer.service;

import com.rbs.transfer.exception.TransferException;

import java.math.BigDecimal;

public interface TransferService {

	void transfer(String source, String target, BigDecimal amount) throws TransferException;

}
