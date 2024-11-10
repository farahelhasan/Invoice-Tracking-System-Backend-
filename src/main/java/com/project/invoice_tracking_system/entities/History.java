package com.project.invoice_tracking_system.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "history")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class History {
	
	@Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  @Column(nullable = false)
	  private Integer id;
	
	  @ManyToOne 
	    @JoinColumn(name = "user_id")
	    private User user; 
	  
	  @ManyToOne 
	    @JoinColumn(name = "item_id")
	    private Item item; 
	  
	  @ManyToOne 
	    @JoinColumn(name = "invoice_id")
	    private Invoice invoice; 
	  
	  private int quantity;
	  private double price;
	  private String action;
	  private String description;
	  private String type; // invoice or item
	  private int status; // 0 or 1 
	  @CreationTimestamp
	    @Column(updatable = false, name = "timestamp")
	    private Date timestamp;
	  
	  public int getQuantity() {
			return quantity;
		}
		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}
		public double getPrice() {
			return price;
		}
		public void setPrice(double price) {
			this.price = price;
		}
		public String getAction() {
			return action;
		}
		public void setAction(String action) {
			this.action = action;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public int getStatus() {
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public User getUser() {
			return user;
		}
		public void setUser(User user) {
			this.user = user;
		}
		public Item getItem() {
			return item;
		}
		public void setItem(Item item) {
			this.item = item;
		}
		public Invoice getInvoice() {
			return invoice;
		}
		public void setInvoice(Invoice invoice) {
			this.invoice = invoice;
		}
		public Date getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(Date timestamp) {
			this.timestamp = timestamp;
		}

}
