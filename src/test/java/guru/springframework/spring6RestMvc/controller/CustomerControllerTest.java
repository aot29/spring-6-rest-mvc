package guru.springframework.spring6RestMvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6RestMvc.model.Customer;
import guru.springframework.spring6RestMvc.services.CustomerService;
import guru.springframework.spring6RestMvc.services.CustomerServiceImpl;
import net.bytebuddy.dynamic.DynamicType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.TestExecutionResult;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    CustomerService customerService;
    CustomerServiceImpl customerServiceImpl;
    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;
    @Captor
    ArgumentCaptor<Customer> customerArgumentCaptor;

    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
    }

    @Test
    void testListCustomers() throws Exception {
        given(customerService.listCustomers()).willReturn(customerServiceImpl.listCustomers());
        mockMvc.perform(get(CustomerController.CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    void testGetCustomerByIdNotFound() throws Exception {
        given(customerService.getCustomerById(any(UUID.class))).willReturn(Optional.empty());
        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCustomerById() throws Exception {
        Customer testCustomer = customerServiceImpl.listCustomers().get(0);
        given(customerService.getCustomerById(testCustomer.getId())).willReturn(Optional.of(testCustomer));
        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, testCustomer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerName", is(testCustomer.getCustomerName())));
    }

    @Test
    void testCreateCustomer() throws Exception {
        Customer customer = customerServiceImpl.listCustomers().get(0);
        customer.setId(null);
        customer.setVersion(null);
        given(customerService.saveCustomer(any(Customer.class))).willReturn(customerServiceImpl.listCustomers().get(1));
        mockMvc.perform(post(CustomerController.CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location")
        );
    }

    @Test
    void testUpdateCustomer() throws Exception {
        Customer customer = customerServiceImpl.listCustomers().get(0);
        mockMvc.perform(put(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isNoContent());
        verify(customerService).updateCustomerById(uuidArgumentCaptor.capture(), any(Customer.class));
        assertEquals(customer.getId(), uuidArgumentCaptor.getValue());
    }

    @Test
    void testDeleteCustomer() throws Exception {
        Customer customer = customerServiceImpl.listCustomers().get(0);
        mockMvc.perform(delete(CustomerController.CUSTOMER_PATH_ID, customer.getId()))
                .andExpect(status().isNoContent());

        verify(customerService).deleteById(uuidArgumentCaptor.capture());
        assertEquals(customer.getId(), uuidArgumentCaptor.getValue());
    }

    @Test
    void testPatchCustomer() throws Exception {
        Customer customer = customerServiceImpl.listCustomers().get(0);
        HashMap<String, Object> map = new HashMap();
        map.put("customerName", "New Customer Name");
        mockMvc.perform(patch(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(map)))
                .andExpect(status().isNoContent());
        verify(customerService).patchCustomerById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());
        assertEquals(customer.getId(), uuidArgumentCaptor.getValue());
        assertEquals(map.get("customerName"), customerArgumentCaptor.getValue().getCustomerName());
    }
}