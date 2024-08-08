package com.hired.onlineshopping.db.dao;

import com.hired.onlineshopping.db.po.OnlineShoppingOrder;

public interface OnlineShoppingOrderDao {
    int deleteOrderById(Long orderId);

    int insertOrder(OnlineShoppingOrder record);

    OnlineShoppingOrder queryOrderById(Long orderId);

    int updateOrder(OnlineShoppingOrder record);
}
