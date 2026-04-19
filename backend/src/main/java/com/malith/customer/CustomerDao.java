package com.malith.customer;


import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> getAllCustomer();

    Optional<Customer> getCustomerById(Integer id);

    void createCustomer(Customer customer);

    boolean isCustomerExistswithEmail(String email);

    boolean isCustomerExistsWithId(Integer id);

    void deleteCustomerById(Integer id);

    void updateCustomer(Customer customerById);
}
