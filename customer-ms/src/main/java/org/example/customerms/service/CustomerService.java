package org.example.customerms.service;

import org.example.customerms.beans.Customer;
import org.example.customerms.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    public List<Customer> findAll(){
        return customerRepo.findAll();
    }

    public Customer findById(Long id){
        return customerRepo.findById(id).orElseThrow(()->new RuntimeException("Customer not found"));
    }

    public Customer save(Customer customer){
        return customerRepo.save(customer);
    }

}
