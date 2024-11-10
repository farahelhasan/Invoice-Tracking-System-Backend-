package com.project.invoice_tracking_system.dots;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetailstDto {

	private List<InvoiceItemDto> invoiceItemsDto;
	private String email;
}
