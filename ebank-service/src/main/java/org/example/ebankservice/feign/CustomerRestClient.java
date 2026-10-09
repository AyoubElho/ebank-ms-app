
package org.example.ebankservice.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.example.ebankservice.model.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CUSTOMER-MS", path = "/customers")
public interface CustomerRestClient {

    @GetMapping("/id/{id}")
    @CircuitBreaker(
            name = "customerService",
            fallbackMethod = "getDefaultCustomer"
    )
    Customer getCustomerById(@PathVariable("id") Long id);

    default Customer getDefaultCustomer(Long id, Throwable e) {
        System.out.println("Fallback triggered: " + e.getMessage());

        return new Customer(id, "NoName", "NoEmail");
    }
}
