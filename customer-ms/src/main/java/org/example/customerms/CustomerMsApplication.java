package org.example.customerms;

import org.example.customerms.beans.Customer;
import org.example.customerms.service.CustomerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.beans.BeanProperty;
import java.util.List;

@SpringBootApplication
public class CustomerMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerMsApplication.class, args);
    }


    @Bean
    CommandLineRunner commandLineRunner(CustomerService customerService) {
        return args -> {

            List<String> names = List.of("ayoub", "saad", "mehdi");
            names.forEach(name -> {
                customerService.save(Customer.builder()
                        .name(name).email(name + "@gmail.com").build());
            });

        };
    }
}
