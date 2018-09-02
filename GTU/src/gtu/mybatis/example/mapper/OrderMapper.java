package gtu.mybatis.example.mapper;

import gtu.mybatis.example.domain.Order;

import java.util.List;

public interface OrderMapper {

    List<Order> getOrdersByUsername(String username);

    Order getOrder(int orderId);

    void insertOrder(Order order);

    void insertOrderStatus(Order order);

}
