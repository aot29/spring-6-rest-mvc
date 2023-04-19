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
@RequestMapping("/api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;

    /**
     * Example
     * curl -H "Content-Type: application/json" --request PATCH --data '{"customerName":"Bob PATCHED"}' http://localhost:8080/api/v1/customer/uuid-xxx
     * */
    @PatchMapping("{customerId}")
    public ResponseEntity patchCustomer(@PathVariable UUID customerId, @RequestBody Customer customer) {
        customerService.patchCustomerById(customerId, customer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * Example:
     * curl -H "Content-Type: application/json" --request DELETE http://localhost:8080/api/v1/customer/fc2b532d-734d-4d52-8e6d-279cca7ce201
     * */
    @DeleteMapping("{customerId}")
    public ResponseEntity deleteById(@PathVariable("customerId") UUID customerId) {
        customerService.deleteById(customerId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * Example
     * curl -H "Content-Type: application/json" --request PUT --data '{"customerName":"Bob UPDATED"}' http://localhost:8080/api/v1/customer/uuid-xxx
     * */
    @PutMapping("{customerId}")
    public ResponseEntity updateCustomer(@PathVariable UUID customerId, @RequestBody Customer customer) {
        customerService.updateCustomerById(customerId, customer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity handlePost(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.saveCustomer(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + savedCustomer.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Customer> listCustomers() {
        log.debug("list customers requested");
        return customerService.listCustomers();
    }

    @RequestMapping(value = "{customerId}", method = RequestMethod.GET)
    public Customer getCustomerById(@PathVariable("customerId")UUID id) {
        return customerService.getCustomerById(id);
    }
}
