package com.hired.onlineshopping.db.dao;

import com.hired.onlineshopping.db.po.OnlineShoppingOrder;
import com.hired.onlineshopping.db.po.OnlineShoppingUser;

public interface OnlineShoppingUserDao {
    int deleteUserById(Long userId);

    int insertUser(OnlineShoppingUser user);

    OnlineShoppingUser queryUserById(Long userId);

    int updateUser(OnlineShoppingUser user);
}
