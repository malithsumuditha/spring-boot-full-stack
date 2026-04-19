package com.malith.customer;

import com.malith.exception.BadRequestException;
import com.malith.exception.DuplicateEntryException;
import com.malith.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerDao customerDao;
    private CustomerServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerServiceImpl(customerDao);
    }

    @Test
    void getAllCustomers() {

        //When
        underTest.getAllCustomers();

        //Then
        verify(customerDao).getAllCustomer();
    }

    @Test
    void getCustomerByID() {
        //Given
        int id = 10;
        Customer customer = new Customer(id,"Malith","malith@gmail.com",25);
        when(customerDao.getCustomerById(id)).thenReturn(Optional.of(customer));

        //When
        Customer actual = underTest.getCustomerByID(id).stream().findFirst().orElseThrow();

        //Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willThrowErrorIfCustomerNotFound() {
        //Given
        int id = 10;

        when(customerDao.getCustomerById(id)).thenReturn(Optional.empty());

        //When

        //Then
        assertThatThrownBy(()-> underTest.getCustomerByID(id)).isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Customer not found");
    }

    @Test
    void createCustomer() {
        //Given
        String email = "malith@gmail.com";

        when(customerDao.isCustomerExistswithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Malith",email,25
        );


        //When
        underTest.createCustomer(request);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).createCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void willThrownEmailExistsErrorWhileCreateCustomer() {
        //Given
        String email = "malith@gmail.com";

        when(customerDao.isCustomerExistswithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Malith",email,25
        );

        //When
        assertThatThrownBy(()-> underTest.createCustomer(request))
                .isInstanceOf(DuplicateEntryException.class)
                .hasMessage("email already exists");

        //Then
        verify(customerDao, never()).createCustomer(any());

    }

    @Test
    void deleteCustomerById() {
        //Given
        int id = 1;
        when(customerDao.isCustomerExistsWithId(id)).thenReturn(true);

        //When
        underTest.deleteCustomerById(id);

        //Then
        verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void willThrowErrorWhileDeleteCustomerById() {
        //Given
        int id = 1;
        when(customerDao.isCustomerExistsWithId(id)).thenReturn(false);

        //When
        assertThatThrownBy(()-> underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                        .hasMessage("Customer not found");

        //Then

        verify(customerDao, never()).deleteCustomerById(id);
    }


    @Test
    void canUpdateCustomerAllProperties() {
        //Given
        int id =1;
        Customer customer = new Customer(
                id,"Malith", "malith@gmail.com", 25
        );
        when(customerDao.getCustomerById(id)).thenReturn(Optional.of(customer));
        String newEmail = "sumud@gmail.com";

        CustomerRegistrationRequest updateRequest = new CustomerRegistrationRequest(
                "Sumu", newEmail, 28
        );

        when(customerDao.isCustomerExistswithEmail(newEmail)).thenReturn(false);

        //When
        underTest.updateCustomer(updateRequest,id);

        //Then
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(argumentCaptor.capture());

        Customer capturedCustomer = argumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());

    }

    @Test
    void canUpdateCustomerNameProperties() {
        //Given
        int id =1;
        Customer customer = new Customer(
                id,"Malith", "malith@gmail.com", 25
        );
        when(customerDao.getCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerRegistrationRequest updateRequest = new CustomerRegistrationRequest(
                "Sumu", "malith@gmail.com", 25
        );

        //When
        underTest.updateCustomer(updateRequest,id);

        //Then
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(argumentCaptor.capture());

        Customer capturedCustomer = argumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());

    }

    @Test
    void canUpdateCustomerEmailProperties() {
        //Given
        int id =1;
        Customer customer = new Customer(
                id,"Malith", "malith@gmail.com", 25
        );
        when(customerDao.getCustomerById(id)).thenReturn(Optional.of(customer));
        String newEmail = "sumud@gmail.com";

        CustomerRegistrationRequest updateRequest = new CustomerRegistrationRequest(
                null, newEmail, null
        );

        when(customerDao.isCustomerExistswithEmail(newEmail)).thenReturn(false);

        //When
        underTest.updateCustomer(updateRequest,id);

        //Then
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(argumentCaptor.capture());

        Customer capturedCustomer = argumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());

    }

    @Test
    void canUpdateCustomerAgeProperties() {
        //Given
        int id =1;
        Customer customer = new Customer(
                id,"Malith", "malith@gmail.com", 25
        );
        when(customerDao.getCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerRegistrationRequest updateRequest = new CustomerRegistrationRequest(
                "Malith", null, 24
        );

        //When
        underTest.updateCustomer(updateRequest,id);

        //Then
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(argumentCaptor.capture());

        Customer capturedCustomer = argumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());

    }

    @Test
    void willThrowEmailAlreadyExistsErrorWhileUpdatingCustomer() {
        //Given
        int id =1;
        Customer customer = new Customer(
                id,"Malith", "malith@gmail.com", 25
        );
        when(customerDao.getCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "mal@gmail.com";
        CustomerRegistrationRequest updateRequest = new CustomerRegistrationRequest(
                null, newEmail, null
        );

        when(customerDao.isCustomerExistswithEmail(updateRequest.email())).thenReturn(true);

        //When
        assertThatThrownBy(()->underTest.updateCustomer(updateRequest,id))
                .isInstanceOf(DuplicateEntryException.class)
                .hasMessage("email already exists");

        //Then
        verify(customerDao, never()).updateCustomer(any());


    }

    @Test
    void willThrowNoDataChangesErrorWhileUpdatingCustomer() {
        //Given
        int id =1;
        Customer customer = new Customer(
                id,"Malith", "malith@gmail.com", 25
        );
        when(customerDao.getCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerRegistrationRequest updateRequest = new CustomerRegistrationRequest(
                null, null, null
        );

        //When
        assertThatThrownBy(()->underTest.updateCustomer(updateRequest,id))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("No data changes");

        //Then
        verify(customerDao, never()).updateCustomer(any());


    }
}