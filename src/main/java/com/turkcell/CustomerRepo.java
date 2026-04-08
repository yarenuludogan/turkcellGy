package com.turkcell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerRepo {
    private Map<Integer, Customer> customers = new HashMap<>();

    public void addCustomer(Customer customer){
        customers.put(customer.getId(), customer);
    }
    public Customer findId(int id){
        return customers.get(id);
    }

    public List<Customer> getAllCustomers(){
        return new ArrayList<>(customers.values());
    }

}
