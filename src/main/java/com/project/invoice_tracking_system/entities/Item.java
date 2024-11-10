package com.project.invoice_tracking_system.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "items")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Item {

	  @Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
      @Column(nullable = false)
      private Integer id;
	  
	  private String itemName;
	  private double price;
	  
}
