package com.hired.onlineshopping.db.dao.impl;

import com.hired.onlineshopping.db.dao.OnlineShoppingOrderDao;
import com.hired.onlineshopping.db.po.OnlineShoppingOrder;
import org.springframework.stereotype.Repository;

@Repository
public class OnlineShoppingOrderDaoMySqlImpl implements OnlineShoppingOrderDao {
    @Override
    public int deleteOrderById(Long orderId) {
        return 0;
    }

    @Override
    public int insertOrder(OnlineShoppingOrder record) {
        return 0;
    }

    @Override
    public OnlineShoppingOrder queryOrderById(Long orderId) {
        return null;
    }

    @Override
    public int updateOrder(OnlineShoppingOrder record) {
        return 0;
    }
}
