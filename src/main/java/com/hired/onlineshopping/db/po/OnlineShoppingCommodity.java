package com.hired.onlineshopping.db.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@Builder
@AllArgsConstructor
public class OnlineShoppingCommodity {
    private Long commodityId;

    private String commodityName;

    private String commodityDesc;

    private Integer price;

    private Integer availableStock;

    private Long creatorUserId;

    private Integer totalStock;

    private Integer lockStock;

    public void setCommodityId(Long commodityId) {
        this.commodityId = commodityId;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName == null ? null : commodityName.trim();
    }

    public void setCommodityDesc(String commodityDesc) {
        this.commodityDesc = commodityDesc == null ? null : commodityDesc.trim();
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setAvailableStock(Integer availableStock) {
        this.availableStock = availableStock;
    }

    public void setCreatorUserId(Long creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public void setTotalStock(Integer totalStock) {
        this.totalStock = totalStock;
    }

    public void setLockStock(Integer lockStock) {
        this.lockStock = lockStock;
    }
}