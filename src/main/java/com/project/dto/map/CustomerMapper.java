package com.project.dto.map;


import com.project.dto.CustomerDTO;
import com.project.entity.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDTO toDto(Customer customer);
    List<CustomerDTO> toDtoList(List<Customer> customers);
}
