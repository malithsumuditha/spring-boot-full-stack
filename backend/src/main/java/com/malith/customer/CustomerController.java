package com.malith.customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Optional<Customer> getCustomer(@PathVariable("id") Integer id){
        return customerService.getCustomerByID(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Integer id){
        customerService.deleteCustomerById(id);
    }

    @PostMapping
    public void createCustomer(@RequestBody CustomerRegistrationRequest customer){
        customerService.createCustomer(customer);
    }

    @PutMapping("/{id}")
    public void updateCustomer(@RequestBody CustomerRegistrationRequest customer, @PathVariable("id")  Integer id){
        customerService.updateCustomer(customer,id);
    }
}
