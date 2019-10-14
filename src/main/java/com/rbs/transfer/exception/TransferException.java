package com.rbs.transfer.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransferException extends RuntimeException {
	public TransferException(String message) {
		super(message);
		log.info(message);
	}
}
