package org.example.customerms.service;

import org.example.customerms.beans.Customer;
import org.example.customerms.repository.CustomerRepo;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepo customerRepo;
    @McpTool(description = "Get all customers")
    public List<Customer> findAll(){
        return customerRepo.findAll();
    }
    @McpTool(description = "Find customer by id")
    public Customer findById(@McpToolParam(description = "The customer id") Long id){
        return customerRepo.findById(id).orElseThrow(()->new RuntimeException("Customer not found"));
    }
    @McpTool(description = "Save a new customer")
    public Customer save(@McpToolParam(description = "The customer to save (name,email)") Customer customer){
        return customerRepo.save(customer);
    }

}
