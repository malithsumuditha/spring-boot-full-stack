package com.malith.customer;

import com.malith.AbstractTestContainers;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerDaoImplTest extends AbstractTestContainers {

    private CustomerDaoImpl underTest;
    private final CustomerRawMapper customerRawMapper = new CustomerRawMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerDaoImpl(getJdbcTemplate(),customerRawMapper);
    }

    @Test
    void getAllCustomer() {
        //Given
        Customer customer = new Customer(
                faker.name().fullName(),
                faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID(),
                20
        );
        underTest.createCustomer(customer);

        //When
        List<Customer> allCustomer = underTest.getAllCustomer();
        System.out.println(allCustomer);

        //Then
        assertThat(allCustomer).isNotEmpty();

    }

    @Test
    //@Order(1)
    void getCustomerById() {
        //Given
        String email = faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();

        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                20
        );
        underTest.createCustomer(customer);

        Integer id = underTest.getAllCustomer().stream().filter(c -> c.getEmail().equals(email))
                .map(Customer::getId).findFirst().orElseThrow();
        System.out.println(id);

        //When
        Customer actual = underTest.getCustomerById(id).orElseThrow();

        //Then
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(id);
        assertThat(actual.getName()).isEqualTo(customer.getName());
        assertThat(actual.getEmail()).isEqualTo(customer.getEmail());
        assertThat(actual.getAge()).isEqualTo(customer.getAge());
    }
    @Test
    void willGetCustomerByIdReturnNull() {
        //Given
        int id = -1;
        //When
        Optional<Customer> actual = underTest.getCustomerById(id);

        //Then
        assertThat(actual).isEmpty();
    }


    @Test
    void createCustomer() {
        //Given
        String email = faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();

        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                20
        );

        //When
        underTest.createCustomer(customer);

        Customer actual = underTest.getAllCustomer().stream()
                .filter(c -> c.getEmail().equals(email)).findFirst().orElseThrow();

        //Then
        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo(customer.getName());
        assertThat(actual.getEmail()).isEqualTo(customer.getEmail());
        assertThat(actual.getAge()).isEqualTo(customer.getAge());

    }

    @Test
    void isCustomerExistswithEmail() {
        //Given
        String email = faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();

        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                20
        );
        underTest.createCustomer(customer);
        //When
        boolean actual = underTest.isCustomerExistswithEmail(email);

        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void isCustomerExistsWithId() {
        //Given
        String email = faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();

        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                20
        );
        underTest.createCustomer(customer);

        Integer id = underTest.getAllCustomer().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId).findFirst().orElseThrow();
        //When
        boolean actual = underTest.isCustomerExistsWithId(id);

        //Then
        assertThat(actual).isTrue();

    }

    @Test
    void deleteCustomerById() {
        //Given
        String email = faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();

        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                20
        );
        underTest.createCustomer(customer);

        Integer id = underTest.getAllCustomer().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId).findFirst().orElseThrow();
        //When
        underTest.deleteCustomerById(id);

        //Then
        boolean actual = underTest.isCustomerExistsWithId(id);
        assertThat(actual).isFalse();
    }

    @Test
    void updateCustomerName() {
        //Given
        String email = faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();

        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                20
        );
        underTest.createCustomer(customer);

        Integer id = underTest.getAllCustomer().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId).findFirst().orElseThrow();

        Customer customerFromDb = underTest.getCustomerById(id).stream().findFirst().orElseThrow();

        String newName = "Malith";

        customerFromDb.setName(newName);

        //When
        underTest.updateCustomer(customerFromDb);

        //Then
        Customer actual = underTest.getCustomerById(id).stream().findFirst().orElseThrow();
        assertThat(actual.getAge()).isEqualTo(customer.getAge());
        assertThat(actual.getName()).isEqualTo(newName);;
        assertThat(actual.getEmail()).isEqualTo(customer.getEmail());
    }

    @Test
    void updateCustomerEmail() {
        //Given
        String email = faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();

        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                20
        );
        underTest.createCustomer(customer);

        Integer id = underTest.getAllCustomer().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId).findFirst().orElseThrow();

        Customer customerFromDb = underTest.getCustomerById(id).stream().findFirst().orElseThrow();

        String newEmail = "mal@gmail.com";

        customerFromDb.setEmail(newEmail);

        //When
        underTest.updateCustomer(customerFromDb);

        //Then
        Customer actual = underTest.getCustomerById(id).stream().findFirst().orElseThrow();
        assertThat(actual.getId()).isEqualTo(id);
        assertThat(actual.getAge()).isEqualTo(customer.getAge());
        assertThat(actual.getName()).isEqualTo(customer.getName());;
        assertThat(actual.getEmail()).isEqualTo(newEmail);
    }

    @Test
    void updateCustomerAge() {
        //Given
        String email = faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();

        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                20
        );
        underTest.createCustomer(customer);

        Integer id = underTest.getAllCustomer().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId).findFirst().orElseThrow();

        Customer customerFromDb = underTest.getCustomerById(id).stream().findFirst().orElseThrow();

        int newAge = 25;

        customerFromDb.setAge(newAge);

        //When
        underTest.updateCustomer(customerFromDb);

        //Then
        Customer actual = underTest.getCustomerById(id).stream().findFirst().orElseThrow();
        assertThat(actual.getId()).isEqualTo(id);
        assertThat(actual.getAge()).isEqualTo(newAge);
        assertThat(actual.getName()).isEqualTo(customer.getName());;
        assertThat(actual.getEmail()).isEqualTo(customer.getEmail());
    }

    @Test
    void updateCustomerAllProperties() {
        //Given
        String email = faker.internet().safeEmailAddress()+"-"+ UUID.randomUUID();

        Customer customer = new Customer(
                faker.name().fullName(),
                email,
                20
        );
        underTest.createCustomer(customer);

        Integer id = underTest.getAllCustomer().stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId).findFirst().orElseThrow();

        Customer customerFromDb = underTest.getCustomerById(id).stream().findFirst().orElseThrow();

        int newAge = 25;
        String newName = "Sumu";
        String newEmail = "malith@gmail.com";

        customerFromDb.setId(id);
        customerFromDb.setAge(newAge);
        customerFromDb.setName(newName);
        customerFromDb.setEmail(newEmail);

        //When
        underTest.updateCustomer(customerFromDb);

        //Then
        Customer actual = underTest.getCustomerById(id).stream().findFirst().orElseThrow();
        assertThat(actual.getId()).isEqualTo(id);
        assertThat(actual.getAge()).isEqualTo(newAge);
        assertThat(actual.getName()).isEqualTo(newName);
        assertThat(actual.getEmail()).isEqualTo(newEmail);
    }
}