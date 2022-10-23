package com.xie.gulimall.search;

import com.xie.gulimall.search.config.GulimallElasticSearchConfig;
import lombok.Data;
import com.alibaba.fastjson.JSON;
import lombok.ToString;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.temporal.ValueRange;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallSearchApplicationTests {
    @Autowired
    RestHighLevelClient client;

    @Data
    @ToString
    public static class Accout {

        private int account_number;
        private int balance;
        private String firstname;
        private String lastname;
        private int age;
        private String gender;
        private String address;
        private String employer;
        private String email;
        private String city;
        private String state;
    }

    @Test
    public void searchData() throws IOException {
        //1.创建检索信息
        SearchRequest searchRequest=new SearchRequest();
        //指定索引
        searchRequest.indices("bank");
        //指定DSL，检索条件
        //SearchSourceBuilder sourceBuilder  封装的条件
        SearchSourceBuilder sourceBuilder=new SearchSourceBuilder();
        //1.1构造检索条件
        sourceBuilder.query(QueryBuilders.matchQuery("address","mill"));
        //sourceBuilder.from();
        //sourceBuilder.size();
        //sourceBuilder.aggregation();  #聚合查询
        //1.2按照年龄的值分布进行聚合
        TermsAggregationBuilder ageAgg = AggregationBuilders.terms("ageAgg").field("age").size(10);
        sourceBuilder.aggregation(ageAgg);

        //1.3计算平均薪资
        AvgAggregationBuilder balanceAvg = AggregationBuilders.avg("balanceAvg").field("balance");
        sourceBuilder.aggregation(balanceAvg);

        searchRequest.source(sourceBuilder);
        System.out.println("检索条件"+sourceBuilder.toString());

        //2.执行检索
        SearchResponse searchResponse = client.search(searchRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);

        //3.分析结果  searchResponse
        System.out.println(searchResponse.toString());
        //3.1获取所有查到的数据
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            String string = hit.getSourceAsString();
            Accout accout = JSON.parseObject(string, Accout.class);
            System.out.println("accout"+accout);
        }

        //3.2获取这次检索到的分析数据
        Aggregations aggregations = searchResponse.getAggregations();
     /*   for (Aggregation aggregation : aggregations.asList()) {
            System.out.println("当前聚合"+aggregation.getName());

        }*/
        Terms ageAgg1 = aggregations.get("ageAgg");
        for (Terms.Bucket bucket : ageAgg1.getBuckets()) {
            String keyAsString = bucket.getKeyAsString();
            System.out.println("年龄"+keyAsString+"==>"+bucket.getDocCount());
        }
        Avg balanceAvg1 = aggregations.get("balanceAvg");
        System.out.println("balanceAvg1 = " + balanceAvg1.getValue());
    }

    /**
     * 测试存储数据到es
     * 更新也可以
     */
    @Test
    public void indexData() throws IOException {
        IndexRequest request = new IndexRequest("users");//索引名
        request.id("1");//文档id
        User user = new User();
        user.setName("张三");
        user.setAge(18);
        user.setGender("男");
        String jsonString = JSON.toJSONString(user);
        request.source(jsonString, XContentType.JSON);//要保存的内容
        //执行操作
        IndexResponse index = client.index(request, GulimallElasticSearchConfig.COMMON_OPTIONS);
        //提取有用的响应数据
        System.out.println(index);
    }

    @Data
    class User{
        private String name;
        private String gender;
        private int age;
    }

    @Test
   public void contextLoads() {
        System.out.println(client);
    }

}
