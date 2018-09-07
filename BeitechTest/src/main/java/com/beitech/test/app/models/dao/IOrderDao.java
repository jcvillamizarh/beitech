package com.beitech.test.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.beitech.test.app.models.entity.Order;

public interface IOrderDao extends CrudRepository<Order, Long> {

}
