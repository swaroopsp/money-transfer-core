package com.rbs.transfer.exception;

import lombok.extern.slf4j.Slf4j;

import java.util.logging.Logger;

@Slf4j
public class AccountException extends RuntimeException {
	public AccountException(String message) {
		super(message);
		log.info(message);
	}
}
