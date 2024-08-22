package com.hired.onlineshopping.db.dao;

import com.hired.onlineshopping.db.po.OnlineShoppingCommodity;

import java.util.List;

public interface OnlineShoppingCommodityDao {
    int insertCommodity(OnlineShoppingCommodity onlineShoppingCommodity);

    List<OnlineShoppingCommodity> listCommodities();

    List<OnlineShoppingCommodity> listCommodityUserId(long userId);

    OnlineShoppingCommodity getCommodityDetail(long commodityId);

    int updateCommodity(OnlineShoppingCommodity commodityDetail);

    int deductStockWithCommodityId(long commodityId);
}
