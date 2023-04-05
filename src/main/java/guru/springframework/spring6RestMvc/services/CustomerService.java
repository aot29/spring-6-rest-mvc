package guru.springframework.spring6RestMvc.services;

import guru.springframework.spring6RestMvc.model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    List<Customer> listCustomers();
    Customer getCustomerById(UUID id);
}
