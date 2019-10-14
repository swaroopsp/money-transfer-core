package com.rbs.transfer.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Setter
@Getter
@Builder
public class Account {
	private String name;
	private BigDecimal balance;
	private List<Movement> transfers = new LinkedList<>();
}
