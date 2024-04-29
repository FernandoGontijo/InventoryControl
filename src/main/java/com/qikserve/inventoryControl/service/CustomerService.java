package com.qikserve.inventoryControl.service;

import com.qikserve.inventoryControl.dto.CustomerDTO;
import com.qikserve.inventoryControl.model.Customer;
import com.qikserve.inventoryControl.repository.CustomerRepository;
import com.qikserve.inventoryControl.util.Util;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<CustomerDTO> findAll() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTO = new ArrayList<>();
        customers.forEach(customer -> customerDTO.add(Util.modelMapper.map(customer, CustomerDTO.class)));
        return customerDTO;
    }

    public CustomerDTO findBy(String id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            return Util.modelMapper.map(customer.get(), CustomerDTO.class);
        } else {
            throw new EntityNotFoundException("Customer not found!");
        }
    }

    public CustomerDTO insert(CustomerDTO customerDTO) {
        checkCustomer(customerDTO);
        Customer customer = Util.modelMapper.map(customerDTO, Customer.class);
        customer.setId(Util.createID());
        Customer customerCreated = customerRepository.save(customer);
        return Util.modelMapper.map(customerCreated, CustomerDTO.class);
    }

    public CustomerDTO update(CustomerDTO customerDTO) {
        CustomerDTO cartToUpdate = findBy(customerDTO.id());
        Customer customer = new Customer();
        customer.setId(cartToUpdate.id());
        customer.setName(customerDTO.name());
        customerRepository.save(customer);
        return Util.modelMapper.map(customer, CustomerDTO.class);
    }

    public void remove(String id) {
        CustomerDTO customerToRemove = findBy(id);
        customerRepository.delete(Util.modelMapper.map(customerToRemove, Customer.class));
    }

    private void checkCustomer(CustomerDTO customerDTO) throws IllegalArgumentException {

        if (customerDTO == null) {
            throw new IllegalArgumentException("Invalid cart!");
        }
        if (customerDTO.name() == null) {
            throw new IllegalArgumentException("Invalid customer name!");
        }
    }


}
