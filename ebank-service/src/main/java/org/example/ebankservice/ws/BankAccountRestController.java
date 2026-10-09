package org.example.ebankservice.ws;

import org.example.ebankservice.beans.BankAccount;
import org.example.ebankservice.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bankAccounts")
public class BankAccountRestController {

    @Autowired
    private BankAccountService bankAccountService;

    @GetMapping("/")
    public List<BankAccount> customerList() {
        return bankAccountService.findAll();
    }

    @GetMapping("/id/{id}")
    public BankAccount findById(@PathVariable String id) {
        return bankAccountService.findById(id);
    }

    @PostMapping("/")
    public BankAccount save(@RequestBody BankAccount bankAccount) {
        return bankAccountService.save(bankAccount);
    }
}