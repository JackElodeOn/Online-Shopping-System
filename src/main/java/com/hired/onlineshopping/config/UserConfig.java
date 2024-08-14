package com.hired.onlineshopping.config;

import com.hired.onlineshopping.model.OnlineShoppingUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    @Bean(name = "zhangsan")
    public OnlineShoppingUser userProvider() {
        return new OnlineShoppingUser("3", "zhangsan", "nobody@gmail.com");
    }

    @Bean(name = "lisi")
    public OnlineShoppingUser userProvider2() {
        return new OnlineShoppingUser("4", "lisi", "nobody@gmail.com");
    }
}
