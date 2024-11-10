package com.project.invoice_tracking_system.entities;

import java.util.ArrayList;
import java.util.List;

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


@Table(name = "invoices")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

	  @Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  @Column(nullable = false)
	  private Integer id;
	  
	  @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<InvoiceItem> items = new ArrayList<>();
	  
	  @ManyToOne 
	    @JoinColumn(name = "user_id")
	    private User user; 
	    
	    private boolean deleted;
	  
	  public void addItem(InvoiceItem item) {
	        items.add(item);
	        item.setInvoice(this);
	    }

	    public void removeItem(InvoiceItem item) {
	        items.remove(item);
	        item.setInvoice(null);
	    }

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public List<InvoiceItem> getItems() {
			return items;
		}

		public void setItems(List<InvoiceItem> items) {
			this.items = items;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public boolean isDeleted() {
			return deleted;
		}

		public void setDeleted(boolean deleted) {
			this.deleted = deleted;
		}
	  
	  
}
