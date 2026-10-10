package org.example.ebankservice.service;

import org.example.ebankservice.beans.BankAccount;
import org.example.ebankservice.feign.CustomerRestClient;
import org.example.ebankservice.model.Customer;
import org.example.ebankservice.repository.BankAccountRepo;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
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

    @McpTool(description = "Get all bank acoounts")
    public List<BankAccount> findAll() {
        return bankAccountRepo.findAll();
    }
    @McpTool(description = "Get a bank acoounts by id")
    public BankAccount findById(@McpToolParam(description = "The bank account id") String id) {
        BankAccount bankAccount = bankAccountRepo.findById(id).orElseThrow(() -> new RuntimeException("BankAccount not found"));
        bankAccount.setCustomer(customerRestClient.getCustomerById(bankAccount.getCustomerId()));
        return bankAccount;
    }
    @McpTool(description = "Save a new bank account")
    public BankAccount save(@McpToolParam(description = "The bank acoount to save (balance , type , customerId)") BankAccount bankAccount) {
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
