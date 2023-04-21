package guru.springframework.spring6RestMvc.controller;

import guru.springframework.spring6RestMvc.model.Customer;
import guru.springframework.spring6RestMvc.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController

public class CustomerController {
    public static final String CUSTOMER_PATH = "/api/v1/customer/";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "{customerId}";

    private final CustomerService customerService;

    /**
     * Example
     * curl -H "Content-Type: application/json" --request PATCH --data '{"customerName":"Bob PATCHED"}' http://localhost:8080/api/v1/customer/uuid-xxx
     * */
    @PatchMapping(CUSTOMER_PATH_ID)
    public ResponseEntity patchCustomer(@PathVariable UUID customerId, @RequestBody Customer customer) {
        customerService.patchCustomerById(customerId, customer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * Example:
     * curl -H "Content-Type: application/json" --request DELETE http://localhost:8080/api/v1/customer/fc2b532d-734d-4d52-8e6d-279cca7ce201
     * */
    @DeleteMapping(CUSTOMER_PATH_ID)
    public ResponseEntity deleteById(@PathVariable("customerId") UUID customerId) {
        customerService.deleteById(customerId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * Example
     * curl -H "Content-Type: application/json" --request PUT --data '{"customerName":"Bob UPDATED"}' http://localhost:8080/api/v1/customer/uuid-xxx
     * */
    @PutMapping(CUSTOMER_PATH_ID)
    public ResponseEntity updateCustomer(@PathVariable UUID customerId, @RequestBody Customer customer) {
        customerService.updateCustomerById(customerId, customer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(CUSTOMER_PATH)
    public ResponseEntity handlePost(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.saveCustomer(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + savedCustomer.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping(CUSTOMER_PATH)
    public List<Customer> listCustomers() {
        log.debug("list customers requested");
        return customerService.listCustomers();
    }

    @GetMapping(CUSTOMER_PATH_ID)
    public Customer getCustomerById(@PathVariable("customerId")UUID id) {

        return customerService.getCustomerById(id).orElseThrow(NotFoundException::new);
    }
}
