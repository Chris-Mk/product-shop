package com.mkolongo.product_shop.repositories;

import com.mkolongo.product_shop.domain.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    Set<Order> findByUser_Username(String user_username);
}
