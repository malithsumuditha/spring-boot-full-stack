package com.malith.customer;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRawMapperTest {

    @Test
    void mapRow() throws SQLException {
        //Given
        CustomerRawMapper customerRawMapper = new CustomerRawMapper();

        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Malith");
        when(resultSet.getInt("age")).thenReturn(25);
        when(resultSet.getString("email")).thenReturn("malith@gmail.com");

        //When
        Customer actual = customerRawMapper.mapRow(resultSet, 1);

        //Then
        Customer expected = new Customer(
                1,"Malith","malith@gmail.com",25
        );

        assertThat(actual).isEqualTo(expected);
    }
}