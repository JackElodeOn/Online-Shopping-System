package com.hired.onlineshopping.controller;

import com.hired.onlineshopping.db.dao.OnlineShoppingCommodityDao;
import com.hired.onlineshopping.db.po.OnlineShoppingCommodity;
import com.hired.onlineshopping.service.EsService;
import com.hired.onlineshopping.service.SearchService;
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

    @Resource
    SearchService searchService;

    @Resource
    EsService esService;

    @GetMapping("/addItem")
    public String createCommodityPage() {
        return "add_commodity";
    }

    @PostMapping("/commodities")
    public String createCommodity(@RequestParam("commodityId") long commodityId,
                                  @RequestParam("commodityName") String commodityName,
                                  @RequestParam("commodityDesc") String commodityDesc,
                                  @RequestParam("price") int price,
                                  @RequestParam("availableStock") int availableStock,
                                  @RequestParam("creatorUserId") long creatorUserId,
                                  Map<String, Object> resultMap) {
        OnlineShoppingCommodity commodity = OnlineShoppingCommodity.builder()
                .commodityId(commodityId)
                .commodityName(commodityName)
                .commodityDesc(commodityDesc)
                .price(price)
                .availableStock(availableStock)
                .creatorUserId(creatorUserId)
                .lockStock(0)
                .totalStock(availableStock)
                .build();
        onlineShoppingCommodityDao.insertCommodity(commodity);
        esService.addCommodityToEs(commodity);
        resultMap.put("Item", commodity);
        return "add_commodity_success";
    }

    @GetMapping("searchAction")
    public String search(@RequestParam("keyWord") String keyWord,
                         Map<String, Object> resultMap) {
        //List<OnlineShoppingCommodity> onlineShoppingCommodities = searchService.searchCommodityMYSQL(keyWord);
        List<OnlineShoppingCommodity> onlineShoppingCommodities = searchService.searchCommodityEs(keyWord);
        resultMap.put("itemList", onlineShoppingCommodities);
        return "search_items";
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

    @PostMapping("updateCommodities/{commodityId}")
    public String updateCommodity(@PathVariable("commodityId") String commodityId,
                                  Map<String, Object> resultMap){
        OnlineShoppingCommodity onlineShoppingCommodity = onlineShoppingCommodityDao.getCommodityDetail(Long.valueOf(commodityId));
        resultMap.put("commodity", onlineShoppingCommodity);
        return "update_commodity";
    }
}
