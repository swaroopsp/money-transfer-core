package com.rbs.transfer.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
	@NotNull
	private String name;
	@NotNull
	private BigDecimal balance;
}
