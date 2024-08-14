package com.hired.onlineshopping.db.dao;

import com.hired.onlineshopping.db.po.OnlineShoppingOrder;

public interface OnlineShoppingOrderDao {
    int deleteOrderById(Long orderId);

    int insertOrder(OnlineShoppingOrder order);

    OnlineShoppingOrder queryOrderByOrderNo(String orderNo);

    int updateOrder(OnlineShoppingOrder order);
}
