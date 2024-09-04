package com.bigdatacompany.elastic;

import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.HashMap;
import java.util.Map;

public class Application {
    public static void main(String[] args)
    {

        RestClientBuilder builder = RestClient.builder(new HttpHost("localhost", 9200, "http"));
        try (RestHighLevelClient client = new RestHighLevelClient(builder)) {

            //// Data Adding Index Document To ElasticSearch

            Map<String, Object> json = new HashMap<>();
            json.put("name", "Dell Inspiration 3585");
            json.put("detail", "intel core i7, 16GB ram, 500GB Hard disk");
            json.put("price", "4300");
            json.put("provider", "Dell Turkey");

            IndexRequest indexRequest = new IndexRequest("products");
            indexRequest.id("3");
            indexRequest.source(json);

            IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);

            System.out.println("Index: " + indexResponse.getIndex());
            System.out.println("ID: " + indexResponse.getId());
            System.out.println("Result: " + indexResponse.getResult().name());

            /////Data Extraction GetAPI with java and ID

            GetRequest getRequest = new GetRequest("products", "1");
            GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);

            if (getResponse.isExists()) {
                Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
                String name = (String) sourceAsMap.get("name");
                String price = (String) sourceAsMap.get("price");
                String detail = (String) sourceAsMap.get("detail");
                String provider = (String)sourceAsMap.get("provider");

                System.out.println("Name: " +name);
                System.out.println("Price: "+price);
                System.out.println("Detail: "+ detail);
                System.out.println("Provide: "+provider);

            } else {
                System.out.println("Product Not Found");
            }

            //// Data Search using Java Search API

            SearchRequest searchRequest = new SearchRequest("products");

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchQuery("provider", "Turkey"));

            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            SearchHit[] hits = searchResponse.getHits().getHits();
            for (SearchHit hit: hits){
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                System.out.println(sourceAsMap);
            }

            ////Data Delete Process

            //Delete Index name and document ID

            DeleteRequest deleteRequest = new DeleteRequest("products", "1");
            DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
            System.out.println("Deleted document ID: " + deleteResponse.getId());

            //// Create DeleteByQueryRequest to delete documents based on query

            DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest("products");
            deleteByQueryRequest.setQuery(QueryBuilders.matchQuery("name","Dell"));

            BulkByScrollResponse bulkByScrollResponse = client.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
            System.out.println("Deleted documents count: " + bulkByScrollResponse.getDeleted());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
