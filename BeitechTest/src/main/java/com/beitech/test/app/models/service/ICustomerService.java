package com.beitech.test.app.models.service;

import java.util.List;
import java.util.Optional;

import com.beitech.test.app.models.entity.Customer;
import com.beitech.test.app.models.entity.Order;
import com.beitech.test.app.models.entity.Product;

public interface ICustomerService {

	public List<Customer> findAll();
	
	public Optional<Customer> findOne(Long id);
	
	public List<Product> findByName(String term);
	
	public void saveOrder(Order order);
	
	public Optional<Product> findProductById(Long id);
	
	public Optional<Order> findOrderById(Long id);
	
}
