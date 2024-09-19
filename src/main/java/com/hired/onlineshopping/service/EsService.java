package com.hired.onlineshopping.service;

import com.alibaba.fastjson.JSON;
import com.hired.onlineshopping.db.po.OnlineShoppingCommodity;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

@Service
@Slf4j
public class EsService {
    @Resource
    RestHighLevelClient restHighLevelClient;

    public static final String COMMODITY_INDEX = "commodity";

    public int addCommodityToEs(OnlineShoppingCommodity commodity) {
        GetIndexRequest getIndexRequest = new GetIndexRequest(COMMODITY_INDEX);
        boolean isIndexExist = false;
        try {
            isIndexExist = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
            if (!isIndexExist) {
                XContentBuilder builder = XContentFactory.jsonBuilder();
                builder.startObject()
                        .startObject("dynamic_templates")
                        .startObject("strings")
                        .field("match_mapping_type", "string")
                        .startObject("mapping")
                        .field("type", "text")
                        .field("analyzer", "ik_smart")
                        .endObject()
                        .endObject()
                        .endObject()
                        .endObject();
                CreateIndexRequest createIndexRequest = new CreateIndexRequest(COMMODITY_INDEX);
                createIndexRequest.source(builder);
                CreateIndexResponse response = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
                if (!response.isAcknowledged()) {
                    log.error("Failed to Create ES Index: " + COMMODITY_INDEX);
                    return RestStatus.INTERNAL_SERVER_ERROR.getStatus();
                }
            }
            String jsonDoc = JSON.toJSONString(commodity);
            IndexRequest indexRequest = new IndexRequest(COMMODITY_INDEX).source(jsonDoc, XContentType.JSON);
            indexRequest.id(commodity.getCommodityId().toString());
            IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            log.info("AddCommodity To ES, commodity: {}, result: {}", jsonDoc, response);
            return response.status().getStatus();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<OnlineShoppingCommodity> searchCommodity(String keyword, int from, int size) {
        SearchRequest searchRequest = new SearchRequest(COMMODITY_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MultiMatchQueryBuilder multiMatchQueryBuilder = multiMatchQuery(keyword, "commodityName", "commodityDesc");
        searchSourceBuilder.query(multiMatchQueryBuilder);
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        searchSourceBuilder.sort("price", SortOrder.ASC);
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            SearchHit[] hits1 = hits.getHits();
            List<OnlineShoppingCommodity> result = new ArrayList<>();
            for (SearchHit hit : hits1) {
                String source = hit.getSourceAsString();
                OnlineShoppingCommodity commodity = JSON.parseObject(source, OnlineShoppingCommodity.class);
                result.add(commodity);
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}