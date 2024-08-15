package com.hired.onlineshopping.controller;

import com.hired.onlineshopping.db.dao.OnlineShoppingCommodityDao;
import com.hired.onlineshopping.db.po.OnlineShoppingCommodity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
public class CommodityController {

    @Resource
    OnlineShoppingCommodityDao onlineShoppingCommodityDao;

    @GetMapping("/addItem")
    public String createCommodityPage() {
        return "add_commodity";
    }

    @PostMapping("/commodities")
    public String createCommodity(@RequestParam("commodityId") long commdityId,
                                  @RequestParam("commodityName") String commodityName,
                                  @RequestParam("commodityDesc") String commodityDesc,
                                  @RequestParam("price") int price,
                                  @RequestParam("availableStock") int availableStock,
                                  @RequestParam("creatorUserId") long creatorUserId,
                                  Map<String, Object> resultMap) {
        OnlineShoppingCommodity commodity = OnlineShoppingCommodity.builder()
                .commodityId(commdityId)
                .commodityName(commodityName)
                .commodityDesc(commodityDesc)
                .price(price)
                .availableStock(availableStock)
                .creatorUserId(creatorUserId)
                .lockStock(0)
                .totalStock(availableStock)
                .build();
        onlineShoppingCommodityDao.insertCommodity(commodity);
        resultMap.put("Item", commodity);
        return "add_commodity_success";
    }

    @GetMapping({"/commodities", "/"})
    public String listCommodities(Map<String, Object> resultMap) {
        List<OnlineShoppingCommodity> onlineShoppingCommodities = onlineShoppingCommodityDao.listCommodities();
        resultMap.put("itemList", onlineShoppingCommodities);
        return "list_items";
    }

    @GetMapping({"sellers/{sellerId}/commodities/", "commodities/{sellerId}" })
    public String listCommoditiesBySellerId(@PathVariable("sellerId") String sellerId,
                                            Map<String, Object> resultMap){
        List<OnlineShoppingCommodity> onlineShoppingCommodities = onlineShoppingCommodityDao.listCommodityUserId(Long.parseLong(sellerId));
        resultMap.put("itemList", onlineShoppingCommodities);
        return "list_items";
    }

    @GetMapping({"item/{commodityId}" })
    public String getCommodity(@PathVariable("commodityId") String commodityId,
                               Map<String, Object> resultMap){

        OnlineShoppingCommodity commodity = onlineShoppingCommodityDao.getCommodityDetail(Long.parseLong(commodityId));
        resultMap.put("commodity", commodity);
        return "item_detail";
    }
}
