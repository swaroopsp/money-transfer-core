package com.rbs.transfer.dto;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferDTO {
	private String from;
	private String to;
	private BigDecimal amount;
}
