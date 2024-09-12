package com.hired.onlineshopping.component;

import com.hired.onlineshopping.db.dao.OnlineShoppingCommodityDao;
import com.hired.onlineshopping.db.po.OnlineShoppingCommodity;
import com.hired.onlineshopping.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
@Slf4j
public class RedisPreHeatRunner implements ApplicationRunner {
    @Resource
    OnlineShoppingCommodityDao commodityDao;

    @Resource
    RedisService redisService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<OnlineShoppingCommodity> onlineShoppingCommodities = commodityDao.listCommodities();
        for ( OnlineShoppingCommodity commodity: onlineShoppingCommodities) {
            String redisKey = "commodity:" +  commodity.getCommodityId();
            redisService.setValue(redisKey, commodity.getAvailableStock().toString());
            log.info("preHeat Staring: Initialize commodity :" + commodity.getCommodityId());
        }
    }
}