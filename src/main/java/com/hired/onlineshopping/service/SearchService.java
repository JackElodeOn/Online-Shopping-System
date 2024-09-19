package com.hired.onlineshopping.service;

import com.hired.onlineshopping.db.dao.OnlineShoppingCommodityDao;
import com.hired.onlineshopping.db.po.OnlineShoppingCommodity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SearchService {

    @Resource
    OnlineShoppingCommodityDao onlineShoppingCommodityDao;

    @Resource
    EsService esService;

    public List<OnlineShoppingCommodity> searchCommodityMYSQL(String keyWord) {
        return onlineShoppingCommodityDao.searchCommodityByKeyWord(keyWord);
    }

    public List<OnlineShoppingCommodity> searchCommodityEs(String keyWord) {
        return esService.searchCommodity(keyWord, 0, 10);
    }
}
