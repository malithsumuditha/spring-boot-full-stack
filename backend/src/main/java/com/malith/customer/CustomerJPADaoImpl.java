package com.malith.customer;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class CustomerJPADaoImpl implements CustomerDao{
    private final CustomerRepository customerRepository;

    public CustomerJPADaoImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> getAllCustomer() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> getCustomerById(Integer id) {
        return Optional.ofNullable(customerRepository.getCustomersById(id));
    }

    @Override
    public void createCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public boolean isCustomerExistswithEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public boolean isCustomerExistsWithId(Integer id) {
        return customerRepository.existsById(id);
    }

    @Override
    public void deleteCustomerById(Integer id) {
        customerRepository.deleteById(id);
    }

    @Override
    public void updateCustomer(Customer customerById) {
        customerRepository.save(customerById);
    }
}
