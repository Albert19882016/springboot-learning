package com.learning.search.esRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.search.annotations.ESDocument;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequestBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.Id;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Repository
public abstract class ElasticsearchScriptFactory<T extends Object> {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

   /*
    Map<String, Object> params = new HashMap<>();
    params.put("num1", 1);
    params.put("num2", 2);
    String inlineScript = "long age;if (doc['age'].value < 45)  age = doc['age'].value + 50; return age * params.num1;";
    Script script = new Script(ScriptType.INLINE,"painless",inlineScript, params);
    ScriptScoreFunctionBuilder scriptScoreFunction = ScoreFunctionBuilders.scriptFunction(script);
    //MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", "中华");
    searchRequestBuilder.setQuery(functionScoreQuery(QueryBuilders.matchQuery("name","中华").operator(Operator.AND),scriptScoreFunction));
    */

    public T queryOne(String script, Map<String, Object> input) {
        String[] params = prepareIndex();
        SearchResponse sr = new SearchTemplateRequestBuilder(elasticsearchTemplate.getClient())
                .setScript(script)
                .setScriptType(ScriptType.STORED)
                .setScriptParams(input)
                .setRequest(new SearchRequest().indices(params[0]).types(params[1]))
                .get()
                .getResponse();
        Long total = sr.getHits().getTotalHits();
        if(total == 0){
            return null;
        }

        //Elasticsearch检索到匹配的结果数
        input.put("es-hit", total);

        SearchHit[] hits = sr.getHits().getHits();
        T t = null;
        try {
            t = mapper.readValue(hits[0].getSourceAsString(),(Class <T>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return t;
    }

    public List<T> queryList(String script,Map<String, Object> input) throws IOException {
        String[] params = prepareIndex();
        SearchResponse sr = new SearchTemplateRequestBuilder(elasticsearchTemplate.getClient())
                .setScript(script)
                .setScriptType(ScriptType.STORED)
                .setScriptParams(input)
                .setRequest(new SearchRequest().indices(params[0]).types(params[1]))
                .get()
                .getResponse();
        Long total = sr.getHits().getTotalHits();
        if(total == 0){
            return null;
        }

        //Elasticsearch检索到匹配的结果数
        input.put("es-hit", total);

        SearchHit[] hits= sr.getHits().getHits();
        List<T> list=new ArrayList<>();
        for(SearchHit hit:hits){
            T t = mapper.readValue(hit.getSourceAsString(),(Class <T>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
            list.add(t);
        }
        return list;
    }

    public T getOneEntity(String id){
        String[] params = prepareIndex() ;
        GetResponse response = elasticsearchTemplate.getClient().prepareGet(params[0],params[1],id).get();
        try {
            T t = mapper.readValue(response.getSourceAsBytes(),(Class <T>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
            return t;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<T> multiGet(Iterable<String> ids){
        String[] params = prepareIndex();
        List<T> list = new ArrayList<>();
        MultiGetResponse multiGetItemResponses = elasticsearchTemplate.getClient().prepareMultiGet().add(params[0],params[1],ids).get();
        for (MultiGetItemResponse itemResponse : multiGetItemResponses) {
            GetResponse response = itemResponse.getResponse();
            if (response.isExists()) {
                try {
                    T t = mapper.readValue(response.getSourceAsBytes(),(Class <T>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
                    list.add(t);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

/*    public boolean save(T t) {
        byte[] json =null;
        try {
            json = mapper.writeValueAsBytes(t);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String[] params = prepareIndex(t);
        IndexResponse response = elasticsearchTemplate.getClient().prepareIndex(params[0],params[1],params[2]).setSource(json,XContentType.JSON).get();
        String result= response.getResult().getLowercase();
        if(result.equals("created")){
            return true;
        }
        return false;
    }*/

    public boolean update(T t) {
        String[] params = prepareIndex(t);
        byte[] json = null;
        try {
            json = mapper.writeValueAsBytes(t);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        UpdateRequest updateRequest = new UpdateRequest(params[0], params[1],params[2]).doc(json,XContentType.JSON);
        UpdateResponse response = null;
        try {
            response = elasticsearchTemplate.getClient().update(updateRequest).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        }
        if(response.getResult().getLowercase().equals("updated")){
            return true;
        }
        return false;
    }

    public boolean delete(String id) {
        String[] params = prepareIndex();
        DeleteResponse response = elasticsearchTemplate.getClient().prepareDelete(params[0],params[1],id).get();
        String result = response.getResult().getLowercase();
        if(result.equals("deleted")){
            return true;
        }
        return false;
    }

    public List<Object> getFieldValue(String script,Map<String,Object> param) {
        String[] params= prepareIndex();
        SearchResponse sr = new SearchTemplateRequestBuilder(elasticsearchTemplate.getClient())
                .setScript(script)
                .setScriptType(ScriptType.STORED)
                .setScriptParams(param)
                .setRequest(new SearchRequest().indices(params[0]).types(params[1]))
                .get()
                .getResponse();
        List<Object> objs = sr.getHits().getHits()[0].getFields().get("start").getValues();
        return objs;
    }

    public Long getAggValue(String script,Map<String,Object> param) throws IOException {
        String[] params = prepareIndex();
        SearchResponse sr = new SearchTemplateRequestBuilder(elasticsearchTemplate.getClient())
                .setScript(script)
                .setScriptType(ScriptType.STORED)
                .setScriptParams(param)
                .setRequest(new SearchRequest().indices(params[0]).types(params[1]))
                .get()
                .getResponse();
        String value = sr.toString();
        Map<String,Object> map = mapper.readValue(value,new TypeReference<HashMap<String, Object>>() {});
        Map<String,Object> map1= (Map<String,Object>)map.get("aggregations");
        Map<String,Object> map2= (Map<String,Object>)map1.get("agg_num");
        Float result = Float.parseFloat(map2.get("value").toString());
        return result.longValue();
    }

    public Long getTotalValue(String script,Map<String,Object> param) throws IOException {
        String[] params = prepareIndex();
        SearchResponse sr = new SearchTemplateRequestBuilder(elasticsearchTemplate.getClient())
                .setScript(script)
                .setScriptType(ScriptType.STORED)
                .setScriptParams(param)
                .setRequest(new SearchRequest().indices(params[0]).types(params[1]))
                .get()
                .getResponse();
        Long num = sr.getHits().getTotalHits();
        return num;
    }

    String[] prepareIndex(T t){
        String index = t.getClass().getAnnotation(ESDocument.class).index();
        String type = t.getClass().getAnnotation(ESDocument.class).type();
        String id = null;
        Field[] fields = t.getClass().getDeclaredFields();
        for(Field f:fields){
            if(f.getAnnotation(Id.class) != null){
                try {
                    f.setAccessible(true);
                    id = f.get(t).toString();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return new String[]{index,type,id};
    }

    String[] prepareIndex(){
        Class <T> entityClass = (Class <T>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        String index = entityClass.getAnnotation(Document.class).indexName();
        String type = entityClass.getAnnotation(Document.class).type();
        return new String[]{index,type};
    }
}
