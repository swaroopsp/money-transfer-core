package com.rbs.transfer.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class Transfer {

	private String origin;
	private String target;
	private BigDecimal amount;
}
