package com.malith.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<Customer> getAllCustomers();
    //Customer getCustomerById(Integer id);

    Optional<Customer> getCustomerByID(Integer id);

    void createCustomer(CustomerRegistrationRequest customer);

    void deleteCustomerById(Integer id);

    void updateCustomer(CustomerRegistrationRequest customer, Integer id);
}
