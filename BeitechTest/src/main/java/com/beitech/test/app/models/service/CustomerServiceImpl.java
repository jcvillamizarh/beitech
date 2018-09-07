package com.beitech.test.app.models.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beitech.test.app.models.dao.ICustomerDao;
import com.beitech.test.app.models.dao.IOrderDao;
import com.beitech.test.app.models.dao.IProductDao;
import com.beitech.test.app.models.entity.Customer;
import com.beitech.test.app.models.entity.Order;
import com.beitech.test.app.models.entity.Product;

/**
 * Servicio para acciones sobre Cliente
 * 
 * @author Juan Camilo villamizar
 *
 */
@Service
public class CustomerServiceImpl implements ICustomerService {

	@Autowired
	private ICustomerDao customerDao;

	@Autowired
	private IProductDao productoDao;

	@Autowired
	private IOrderDao orderDao;

	@Override
	@Transactional(readOnly = true)
	public List<Customer> findAll() {
		return (List<Customer>) customerDao.findAll();
	}

	@Override
	public Optional<Customer> findOne(Long id) {
		return customerDao.findById(id);
	}

	@Override
	public List<Product> findByName(String term) {
		return productoDao.findByName(term);
	}

	@Override
	@Transactional
	public void saveOrder(Order order) {
		orderDao.save(order);

	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Product> findProductById(Long id) {
		return productoDao.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Order> findOrderById(Long id) {

		return orderDao.findById(id);
	}

}
