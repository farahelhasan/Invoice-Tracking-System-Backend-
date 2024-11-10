package com.project.invoice_tracking_system.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "roles")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Role {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

   

}
