package com.hired.onlineshopping.service;

import com.hired.onlineshopping.db.dao.OnlineShoppingCommodityDao;
import com.hired.onlineshopping.db.dao.OnlineShoppingOrderDao;
import com.hired.onlineshopping.db.po.OnlineShoppingCommodity;
import com.hired.onlineshopping.db.po.OnlineShoppingOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@Service
public class OrderService {

    @Resource
    OnlineShoppingOrderDao orderDao;

    @Resource
    OnlineShoppingCommodityDao commodityDao;

    public OnlineShoppingOrder placeOrderOriginal(String commodityId, String userId) {
        OnlineShoppingCommodity commodityDetail = commodityDao.getCommodityDetail(Long.parseLong(commodityId));
        int availableStock = commodityDetail.getAvailableStock();
        int lockStock = commodityDetail.getLockStock();
        if (availableStock > 0) {
            availableStock--;
            lockStock++;
            commodityDetail.setAvailableStock(availableStock);
            commodityDetail.setLockStock(lockStock);
            commodityDao.updateCommodity(commodityDetail);
            OnlineShoppingOrder order = createOrder(commodityId, 1L, userId);
            orderDao.insertOrder(order);
            return order;
        } else {
            return null;
        }
    }

    private OnlineShoppingOrder createOrder(String commodityId, long orderAmount, String userId) {
        OnlineShoppingOrder order = OnlineShoppingOrder.builder()
                .orderNo(UUID.randomUUID().toString())
                .commodityId(Long.valueOf(commodityId))
                .orderAmount(orderAmount)
                .orderStatus(1)
                // 0: invalid order
                // 1. pending payment
                // 2. finish payment
                // 99. overtime order
                .createTime(new Date())
                .userId(Long.valueOf(userId))
                .build();
        return order;
    }

    public OnlineShoppingOrder queryOrderByOrderNo(String orderNo) {
        return orderDao.queryOrderByOrderNo(orderNo);
    }

    public void payOrder(String orderNum) {
        // update Order status in MySQL
        OnlineShoppingOrder order = queryOrderByOrderNo(orderNum);
        order.setOrderStatus(2);
        order.setPayTime(new Date());
        orderDao.updateOrder(order);
        // update commodity lockstock in mysql
        OnlineShoppingCommodity commodityDetail = commodityDao.getCommodityDetail(order.getCommodityId());
        commodityDetail.setLockStock(commodityDetail.getLockStock() - 1);
    }
}
