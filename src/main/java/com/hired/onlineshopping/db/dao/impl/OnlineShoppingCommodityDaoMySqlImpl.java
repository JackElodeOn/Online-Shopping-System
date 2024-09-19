package com.hired.onlineshopping.db.dao.impl;

import com.hired.onlineshopping.OnlineShoppingApplication;
import com.hired.onlineshopping.db.dao.OnlineShoppingCommodityDao;
import com.hired.onlineshopping.db.mappers.OnlineShoppingCommodityMapper;
import com.hired.onlineshopping.db.mappers.OnlineShoppingUserMapper;
import com.hired.onlineshopping.db.po.OnlineShoppingCommodity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class OnlineShoppingCommodityDaoMySqlImpl implements OnlineShoppingCommodityDao {
    @Resource
    OnlineShoppingCommodityMapper onlineShoppingCommodityMapper;

    @Override
    public int insertCommodity(OnlineShoppingCommodity onlineShoppingCommodity) {
        return onlineShoppingCommodityMapper.insert(onlineShoppingCommodity);
    }

    @Override
    public List<OnlineShoppingCommodity> listCommodities() {
        return onlineShoppingCommodityMapper.listCommodities();
    }

    @Override
    public List<OnlineShoppingCommodity> listCommodityUserId(long userId) {
        return onlineShoppingCommodityMapper.listCommodityUserId(userId);
    }

    @Override
    public OnlineShoppingCommodity getCommodityDetail(long commodityId) {
        return onlineShoppingCommodityMapper.selectByPrimaryKey(commodityId);
    }

    @Override
    public int updateCommodity(OnlineShoppingCommodity commodityDetail) {
        return onlineShoppingCommodityMapper.updateByPrimaryKey(commodityDetail);
    }

    @Override
    public int deductStockWithCommodityId(long commodityId) {
        return onlineShoppingCommodityMapper.deductStockWithCommodityId(commodityId);
    }

    @Override
    public int revertStockWithCommodityId(long commodityId) {
        return onlineShoppingCommodityMapper.revertStockWithCommodityId(commodityId);
    }

    @Override
    public List<OnlineShoppingCommodity> searchCommodityByKeyWord(String keyWord) {
        String keyWordPattern = "%" + keyWord + "%";
        return onlineShoppingCommodityMapper.searchCommodityByKeyWord(keyWordPattern);
    }
}
