package com.beitech.test.app.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.beitech.test.app.models.entity.Customer;

public interface ICustomerDao extends PagingAndSortingRepository<Customer, Long> {

}
