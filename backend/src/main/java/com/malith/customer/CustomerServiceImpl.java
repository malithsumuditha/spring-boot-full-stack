package com.malith.customer;

import com.malith.exception.BadRequestException;
import com.malith.exception.DuplicateEntryException;
import com.malith.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDao customerDao;

    public CustomerServiceImpl(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerDao.getAllCustomer();
    }

    @Override
    public Optional<Customer> getCustomerByID(Integer id) {
        Optional<Customer> customerById = customerDao.getCustomerById(id);

        if(customerById.isEmpty()){
            log.warn("Customer not found with id: {}", id);
            throw new ResourceNotFoundException("Customer not found");
        }
        log.info("Customer found....");
       return customerById;
    }

    @Override
    public void createCustomer(CustomerRegistrationRequest customer) {

        String email = customer.email();
        if(customerDao.isCustomerExistswithEmail(email)){
            throw new DuplicateEntryException("email already exists");
        }

        Customer customer1 = new Customer(
                customer.name(),
                customer.email(),
                customer.age()
        );
        customerDao.createCustomer(customer1);
    }

    @Override
    public void deleteCustomerById(Integer id) {
        if(!customerDao.isCustomerExistsWithId(id)){
            throw new ResourceNotFoundException("Customer not found");
        }

        customerDao.deleteCustomerById(id);
    }

    @Override
    public void updateCustomer(CustomerRegistrationRequest customer, Integer id) {
        Customer customerById = customerDao.getCustomerById(id).orElseThrow(() -> new ResourceNotFoundException(
                "Customer not found"
        ));

        boolean haveChanges = false;

        if (customer.name() != null && !customerById.getName().equals(customer.name())){
            haveChanges = true;
            customerById.setName(customer.name());
        }
        if (customer.email() != null && !customerById.getEmail().equals(customer.email())){
            if(customerDao.isCustomerExistswithEmail(customer.email())){
                throw new DuplicateEntryException("email already exists");
            }
            haveChanges = true;
            customerById.setEmail(customer.email());
        }
        if (customer.age() != null && !customerById.getAge().equals(customer.age())){
            haveChanges = true;
            customerById.setAge(customer.age());
        }

        if(haveChanges){
            customerDao.updateCustomer(customerById);
        }else{
            throw new BadRequestException("No data changes");
        }

    }
}
