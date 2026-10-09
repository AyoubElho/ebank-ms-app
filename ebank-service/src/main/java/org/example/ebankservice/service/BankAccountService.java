package org.example.ebankservice.service;

import org.example.ebankservice.beans.BankAccount;
import org.example.ebankservice.feign.CustomerRestClient;
import org.example.ebankservice.model.Customer;
import org.example.ebankservice.repository.BankAccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepo bankAccountRepo;
    @Autowired
    private CustomerRestClient customerRestClient;


    public List<BankAccount> findAll() {
        return bankAccountRepo.findAll();
    }

    public BankAccount findById(String id) {
        BankAccount bankAccount = bankAccountRepo.findById(id).orElseThrow(() -> new RuntimeException("BankAccount not found"));
        bankAccount.setCustomer(customerRestClient.getCustomerById(bankAccount.getCustomerId()));
        return bankAccount;
    }

    public BankAccount save(BankAccount bankAccount) {
        try {
            Customer customer = customerRestClient.getCustomerById(bankAccount.getCustomerId());
            bankAccount.setId(UUID.randomUUID().toString());
            bankAccount.setCreatedAt(new Date());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());

        }

        return bankAccountRepo.save(bankAccount);
    }

}
