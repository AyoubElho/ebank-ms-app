package org.example.ebankservice.repository;

import org.example.ebankservice.beans.BankAccount;
import org.example.ebankservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepo extends JpaRepository<BankAccount,String> {

    public List<BankAccount> findByCustomerId(Long id);
}
