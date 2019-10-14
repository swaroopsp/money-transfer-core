package com.rbs.transfer.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Setter
@Getter
public class Movement {

	private String account;
	private BigDecimal amount;
	private MovementType type;

	public enum MovementType {
		INBOUND, OUTBOUND;
	}
}
