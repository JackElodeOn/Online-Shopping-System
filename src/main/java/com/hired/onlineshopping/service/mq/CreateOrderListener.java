package com.hired.onlineshopping.service.mq;

import com.alibaba.fastjson.JSON;
import com.hired.onlineshopping.db.dao.OnlineShoppingCommodityDao;
import com.hired.onlineshopping.db.dao.OnlineShoppingOrderDao;
import com.hired.onlineshopping.db.po.OnlineShoppingOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.locks.LockSupport;

@Component
@Slf4j
@RocketMQMessageListener(topic = "createOrder", consumerGroup = "createOrderGroup")
public class CreateOrderListener implements RocketMQListener<MessageExt>, RocketMQPushConsumerLifecycleListener {
    @Resource
    OnlineShoppingOrderDao orderDao;

    @Resource
    RocketMQService rocketMQService;

    @Resource
    OnlineShoppingCommodityDao onlineShoppingCommodityDao;

    @Override
    public void onMessage(MessageExt messageExt) {
        // 1. Create order to DB
        String body = new String(messageExt.getBody());
        log.info("createOrder Message body: " + body);
        OnlineShoppingOrder order = JSON.parseObject(body, OnlineShoppingOrder.class);
        order.setCreateTime(new Date());
        int res = onlineShoppingCommodityDao.deductStockWithCommodityId(order.getCommodityId());
        if (res > 0) {
            order.setOrderStatus(1);
            order.setOrderAmount(1L);
            orderDao.insertOrder(order);
            rocketMQService.sendDelayMessage("paymentCheck", JSON.toJSONString(order), 4);
        }
        // 2. send delay to checkOrder message
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
