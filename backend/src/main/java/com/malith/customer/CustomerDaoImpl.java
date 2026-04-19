package com.malith.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerDaoImpl implements CustomerDao{

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRawMapper customerRawMapper;

    public CustomerDaoImpl(JdbcTemplate jdbcTemplate, CustomerRawMapper customerRawMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRawMapper = customerRawMapper;
    }

    @Override
    public List<Customer> getAllCustomer() {
        String sql = "SELECT * FROM customer";
        return jdbcTemplate.query(sql,customerRawMapper);
    }

    @Override
    public Optional<Customer> getCustomerById(Integer id) {
        String sql = "SELECT * FROM customer WHERE id = ?";
        return jdbcTemplate.query(sql,customerRawMapper,id).stream().findFirst();
    }

    @Override
    public void createCustomer(Customer customer) {
        String sql ="INSERT INTO customer (name, email, age) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getAge());
    }

    @Override
    public boolean isCustomerExistswithEmail(String email) {
        String sql = "SELECT COUNT(id) FROM customer WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count >0;
    }

    @Override
    public boolean isCustomerExistsWithId(Integer id) {
        String sql = "SELECT COUNT(id) FROM customer WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public void deleteCustomerById(Integer id) {
        String sql = "DELETE FROM customer WHERE id = ?";
        jdbcTemplate.update(sql,id);
    }

    @Override
    public void updateCustomer(Customer customerById) {
        String sql = "UPDATE customer SET name = ?, email = ?, age = ? WHERE id = ?";
        jdbcTemplate.update(sql,customerById.getName(), customerById.getEmail(), customerById.getAge(), customerById.getId());
    }
}
