package com.hired.onlineshopping.db.mappers;

import com.hired.onlineshopping.db.po.OnlineShoppingUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class OnlineShoppingUserMapperTest {

    @Autowired
    OnlineShoppingUserMapper mapper;

    @Test
    void insert() {
        OnlineShoppingUser user = OnlineShoppingUser.builder()
                .email("noId@gmail.com")
                .userType(1)
                .name("123")
                .address("123")
                .phone("123")
                .build();
        mapper.insert(user);
    }

    @Test
    void selectByPrimaryKey() {
        log.info(mapper.selectByPrimaryKey(123L).toString());
    }
}