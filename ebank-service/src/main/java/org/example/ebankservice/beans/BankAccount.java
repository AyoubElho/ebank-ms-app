package org.example.ebankservice.beans;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.*;
import org.example.ebankservice.model.Customer;

import java.util.Date;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BankAccount {
    @Id
    private String id ;
    private Date createdAt ;
    private double balance  ;
    private String type  ;
    private Long customerId  ;
    @Transient
    private Customer customer;
}

