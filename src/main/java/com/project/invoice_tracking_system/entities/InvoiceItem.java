package com.project.invoice_tracking_system.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Table(name = "invoice_items")
@Entity
@NoArgsConstructor
@AllArgsConstructor

//associative entity (junction table with payload).
public class InvoiceItem {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer id;
	
	private int quantity;
	
	@ManyToOne 
    @JoinColumn(name = "item_id")
    private Item item; 
	
	@ManyToOne 
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

	////////////////////////////////////////
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	} 

	
	//////
	public Invoice returnInvoice() {
		return invoice;
	}
	
	
}
