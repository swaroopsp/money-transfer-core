package com.rbs.transfer.dto;

import lombok.*;

import java.math.BigDecimal;
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

	private String name;
	private BigDecimal balance;
}
