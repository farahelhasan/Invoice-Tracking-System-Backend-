package com.project.invoice_tracking_system.dots;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemDto {
    private int itemId;
    private int quantity;

}
