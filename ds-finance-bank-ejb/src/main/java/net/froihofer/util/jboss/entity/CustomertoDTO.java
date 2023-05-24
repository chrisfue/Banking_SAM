package net.froihofer.util.jboss.entity;

import net.froihofer.bank.common.CustomerDTO;

public class CustomertoDTO {

    public Customer toEntity(CustomerDTO customer) {
        if (customer == null) return null;
        return new Customer(/*customer.getId(),*/ customer.getFirstName(),customer.getLastName(), customer.getAddress());
    }

    /** Converts an entity instance to a DTO instance. */
    public CustomerDTO toDTO(Customer customer) {
        if (customer == null) return null;
        return new CustomerDTO(/*customer.getCustomerId(),*/ customer.getFirstName(),customer.getLastName(), customer.getAddress());
    }
}
