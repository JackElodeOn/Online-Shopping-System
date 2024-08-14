package com.hired.onlineshopping.db.dao.impl;

import com.hired.onlineshopping.db.dao.OnlineShoppingOrderDao;
import com.hired.onlineshopping.db.mappers.OnlineShoppingOrderMapper;
import com.hired.onlineshopping.db.po.OnlineShoppingOrder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class OnlineShoppingOrderDaoMySqlImpl implements OnlineShoppingOrderDao {
    @Resource
    OnlineShoppingOrderMapper onlineShoppingOrderMapper;

    @Override
    public int deleteOrderById(Long orderId) {
        return 0;
    }

    @Override
    public int insertOrder(OnlineShoppingOrder order) {
        return onlineShoppingOrderMapper.insert(order);
    }

    @Override
    public OnlineShoppingOrder queryOrderByOrderNo(String orderNo) {
        return onlineShoppingOrderMapper.selectByOrderNo(orderNo);
    }

    @Override
    public int updateOrder(OnlineShoppingOrder order) {
        return onlineShoppingOrderMapper.updateByPrimaryKey(order);
    }
}
