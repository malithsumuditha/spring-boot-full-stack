package com.malith.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomerListDataAccessService {


    private static List<Customer> customers;

    static {
        customers = new ArrayList<>();
        Customer malith = new Customer(1, "Malith", "malith@gmail.com", 21);
        Customer udara = new Customer(1, "Udara", "udara@gmail.com", 25);

        customers.add(malith);
        customers.add(udara);
    }
//    @Override
//    public List<Customer> getAllCustomer() {
//        return customers;
//    }
//
//    @Override
//    public Optional<Customer> getCustomerById(Integer id) {
//        return customers.stream().filter(customer -> customer.getId().equals(id)).findFirst();
//    }
}
