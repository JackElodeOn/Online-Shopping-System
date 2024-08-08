package com.hired.onlineshopping.db.dao.impl;

import com.hired.onlineshopping.db.dao.OnlineShoppingUserDao;
import com.hired.onlineshopping.db.po.OnlineShoppingUser;
import org.springframework.stereotype.Repository;

@Repository
public class OnlineShoppingUserDaoMySqlImpl implements OnlineShoppingUserDao {
    @Override
    public int deleteUserById(Long userId) {
        return 0;
    }

    @Override
    public int insertUser(OnlineShoppingUser user) {
        return 0;
    }

    @Override
    public OnlineShoppingUser queryUserById(Long userId) {
        return null;
    }

    @Override
    public int updateUser(OnlineShoppingUser user) {
        return 0;
    }
}
