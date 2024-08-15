package com.hired.onlineshopping.db.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hired.onlineshopping.db.mappers.OnlineShoppingCommodityMapper;
import com.hired.onlineshopping.db.mappers.OnlineShoppingOrderMapper;
import com.hired.onlineshopping.db.po.OnlineShoppingCommodity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class OnlineShoppingCommodityDaoTest {
    @Resource
    OnlineShoppingCommodityDao dao;

    @AfterAll
    public static void cleanup() {

    }

    @Test
    void insertCommodity() {
        OnlineShoppingCommodity commodity = OnlineShoppingCommodity.builder()
                .price(123)
                .commodityDesc("desc")
                .commodityName("TestCommodity")
                .creatorUserId(123L)
                .availableStock(111)
                .totalStock(123)
                .lockStock(123)
                .build();
        dao.insertCommodity(commodity);
    }

    @Test
    void listCommodities() {
        log.info(dao.listCommodities().toString());
    }

    @Test
    void listCommodityUserId() {
        log.info(dao.listCommodityUserId(123).toString());
    }

    @Test
    void getCommodityDetail() {
    }
}