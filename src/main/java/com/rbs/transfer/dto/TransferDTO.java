package com.rbs.transfer.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferDTO {
	@NotNull
	private String from;
	@NotNull
	private String to;
	@NotNull
	private BigDecimal amount;
}
