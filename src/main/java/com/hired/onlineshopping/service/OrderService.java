package com.hired.onlineshopping.service;

import com.alibaba.fastjson.JSON;
import com.hired.onlineshopping.db.dao.OnlineShoppingCommodityDao;
import com.hired.onlineshopping.db.dao.OnlineShoppingOrderDao;
import com.hired.onlineshopping.db.mappers.OnlineShoppingOrderMapper;
import com.hired.onlineshopping.db.po.OnlineShoppingCommodity;
import com.hired.onlineshopping.db.po.OnlineShoppingOrder;
import com.hired.onlineshopping.service.mq.RocketMQService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class OrderService {

    @Resource
    OnlineShoppingOrderDao orderDao;

    @Resource
    OnlineShoppingCommodityDao commodityDao;

    @Resource
    RedisService redisService;

    @Resource
    RocketMQService rocketMQService;

    @Autowired
    private OnlineShoppingOrderMapper onlineShoppingOrderMapper;

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
            log.info("Place order succesful, current availableStock:" +  availableStock);
            return order;
        } else {
            log.info("commodity out of stock, commodityId:" + commodityDetail.getCommodityId());
            return null;
        }
    }

    public OnlineShoppingOrder placeOrderOneSQL(String commodityId, String userId) {
        int result = commodityDao.deductStockWithCommodityId(Long.parseLong(commodityId));
        if (result > 0) {
            OnlineShoppingOrder order = createOrder(commodityId, 1L, userId);
            orderDao.insertOrder(order);
            log.info("Place order successfully");
            return order;
        } else {
            log.info("Fail to place order, out of stock");
            return null;
        }
    }

    public OnlineShoppingOrder placeOrderWithRedis(String commodityId, String userId) {
        String redisKey = "commodity:" +  commodityId;
        long deductedStock = redisService.stockDeduct(redisKey);
        if (deductedStock >= 0) {
            // TODO: improve write operation deduct with MYSQL to
            return placeOrderOriginal(commodityId, userId);
        }
        else {
            log.info("Process order with Redis shows commodity out of stock");
            return null;
        }
    }

    public OnlineShoppingOrder placeOrderWithDistributedLock(String commodityId, String userId) {
        String lockKey = "lock_commodityId" +  commodityId;
        String requestId = UUID.randomUUID().toString();
        boolean result = redisService.tryToGetDistributedLock(lockKey, requestId, 5000);
        if (result) {
            OnlineShoppingOrder order = placeOrderOriginal(commodityId, userId);
            redisService.releaseDistributedLock(lockKey, requestId);
            return order;
        }
        log.info("Fail to place order with lock key, please try again later, commodityId:" + commodityId);
        return null;
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
        commodityDao.updateCommodity(commodityDetail);
    }

    public OnlineShoppingOrder placeOrderFinal(String commodityId, String userId) {
        String redisKey = "commodity:" +  commodityId;
        long deductedStock = redisService.stockDeduct(redisKey);
        if (deductedStock >= 0) {
            OnlineShoppingOrder order = OnlineShoppingOrder.builder()
                    .orderNo(UUID.randomUUID().toString())
                    .commodityId(Long.parseLong(commodityId))
                    .userId(Long.parseLong(userId))
                    .build();
            try {
                rocketMQService.sendMessage("createOrder", JSON.toJSONString(order));
                redisService.addToDenyList(userId, commodityId);
                return order;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else {
            log.info("Process order with Redis shows commodity out of stock");
            return null;
        }
    }
}
