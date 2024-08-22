package com.hired.onlineshopping.controller;

import com.hired.onlineshopping.db.dao.OnlineShoppingCommodityDao;
import com.hired.onlineshopping.db.dao.OnlineShoppingOrderDao;
import com.hired.onlineshopping.db.po.OnlineShoppingCommodity;
import com.hired.onlineshopping.db.po.OnlineShoppingOrder;
import com.hired.onlineshopping.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Controller
public class OrderController {

    @Resource
    OrderService orderService;

    @Resource
    OnlineShoppingCommodityDao commodityDao;

    @GetMapping("commodity/buy/{userId}/{commodityId}")
    public String buyCommodity(@PathVariable("userId") String userId,
                               @PathVariable("commodityId") String commodityId,
                               Map<String, Object> resultMap) {
        //OnlineShoppingOrder order = orderService.placeOrderOriginal(commodityId, userId);
        //OnlineShoppingOrder order = orderService.placeOrderOneSQL(commodityId, userId);
        //OnlineShoppingOrder order = orderService.placeOrderWithRedis(commodityId, userId);
        OnlineShoppingOrder order = orderService.placeOrderWithDistributedLock(commodityId, userId);
        if (order != null) {
            resultMap.put("resultInfo", "Create Order successfully! OrderNum:" + order.getOrderNo());
            resultMap.put("orderNo", order.getOrderNo());
        } else {
            resultMap.put("resultInfo", "Out of Stock");
            resultMap.put("orderNo", "");
        }
        return "order_result";
    }

    @RequestMapping("commodity/orderQuery/{orderNum}")
    public String orderQuery(@PathVariable("orderNum") String orderNum,
                             Map<String, Object> resultMap) {
        OnlineShoppingOrder order = orderService.queryOrderByOrderNo(orderNum);
        OnlineShoppingCommodity commodityDetail = commodityDao.getCommodityDetail(order.getCommodityId());
        resultMap.put("order", order);
        resultMap.put("commodity", commodityDetail);
        return "order_check";
    }

    @GetMapping("commodity/payOrder/{orderNum}")
    public String payOrder(@PathVariable("orderNum") String orderNum, Map<String, Object> resultMap) {
        orderService.payOrder(orderNum);
        return orderQuery(orderNum, resultMap);
    }

}
