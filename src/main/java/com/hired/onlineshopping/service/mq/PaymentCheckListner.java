package com.hired.onlineshopping.service.mq;

import com.alibaba.fastjson.JSON;
import com.hired.onlineshopping.db.dao.OnlineShoppingCommodityDao;
import com.hired.onlineshopping.db.dao.OnlineShoppingOrderDao;
import com.hired.onlineshopping.db.po.OnlineShoppingOrder;
import com.hired.onlineshopping.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.locks.LockSupport;

@Component
@Slf4j
@RocketMQMessageListener(topic = "paymentCheck", consumerGroup = "paymentCheckGroup")
public class PaymentCheckListner implements RocketMQListener<MessageExt>, RocketMQPushConsumerLifecycleListener {
    @Resource
    OnlineShoppingOrderDao orderDao;

    @Resource
    OnlineShoppingCommodityDao commodityDao;

    @Resource
    RedisService redisService;

    @Override
    public void onMessage(MessageExt messageExt) {
        String body = new String(messageExt.getBody());
        log.info("Receive message: {}", body);
        OnlineShoppingOrder orderMsg = JSON.parseObject(body, OnlineShoppingOrder.class);
        OnlineShoppingOrder order = orderDao.queryOrderByOrderNo(orderMsg.getOrderNo());
        if (order == null) {
            log.error("Can't find order in database");
            return;
        }

        if (order.getOrderStatus() != 2) {
            log.info("Didn't pay the order on time, order number: " + order.getOrderNo());
            order.setOrderStatus(99);
            orderDao.updateOrder(order);
            commodityDao.revertStockWithCommodityId(order.getCommodityId());
            String redisKey = "commodity:" + order.getCommodityId();
            redisService.revertStock(redisKey);
            redisService.removeFromDenyList(order.getUserId().toString(), order.getCommodityId().toString());
        } else {
            log.info("Skip order check since it is already marked finished" + JSON.toJSONString(order));
        }
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
// 在此方法中可以设置一些消费者的属性
        consumer.setMaxReconsumeTimes(3);  // 设置最大重试次数
        consumer.setConsumeTimeout(5000); // 设置消费超时时间为5 秒
        consumer.setConsumeThreadMin(2);
        consumer.setConsumeThreadMax(2 );
    }
}
