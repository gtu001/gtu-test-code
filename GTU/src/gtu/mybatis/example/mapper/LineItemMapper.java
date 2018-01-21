package gtu.mybatis.example.mapper;

import gtu.mybatis.example.domain.LineItem;

import java.util.List;

public interface LineItemMapper {

    List<LineItem> getLineItemsByOrderId(int orderId);

    void insertLineItem(LineItem lineItem);

}
