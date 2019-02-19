package com.learning.search.ESRepository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.search.annotations.ESDocument;
import java.lang.reflect.Field;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequestBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import javax.persistence.Id;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class ESRepository<T extends Object> {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public String query(String script,Map<String, Object> temParams) {
        String[] params = prepareIndex();
        SearchResponse sr = new SearchTemplateRequestBuilder(elasticsearchTemplate.getClient())
                .setScript(script)
                .setScriptType(ScriptType.STORED)
                .setScriptParams(temParams)
                .setRequest(new SearchRequest().indices(params[0]).types(params[1]))
                .get()
                .getResponse();
        SearchHit[] hits= sr.getHits().getHits();
        long total= sr.getHits().getTotalHits();
        String s="{\"total\":"+total+",\"data\":[";
        for(int i=0;i<hits.length;i++){
            s += hits[i].getSourceAsString();
            //hits[i].getSourceAsMap();
            if(i!=hits.length-1){
                s+=",";
            }
        }
        s += "]}";
        return s;
    }

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

    public T queryOne(String script,Map<String, Object> temParams,Map<String,Long> mtotal) throws IOException {
        String[] params = prepareIndex();
        SearchResponse sr = new SearchTemplateRequestBuilder(elasticsearchTemplate.getClient())
                .setScript(script)
                .setScriptType(ScriptType.STORED)
                .setScriptParams(temParams)
                .setRequest(new SearchRequest().indices(params[0]).types(params[1]))
                .get()
                .getResponse();
        Long total = sr.getHits().getTotalHits();
        if(total==0){
            return null;
        }
        if(mtotal!=null){
            mtotal.put("total",total);
        }
        SearchHit[] hits = sr.getHits().getHits();
        T t = mapper.readValue(hits[0].getSourceAsString(),(Class <T>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        return t;
    }

    public List<T> queryList(String script,Map<String, Object> temParams,Map<String,Long> mtotal) throws IOException {
        String[] params = prepareIndex();
        SearchResponse sr = new SearchTemplateRequestBuilder(elasticsearchTemplate.getClient())
                .setScript(script)
                .setScriptType(ScriptType.STORED)
                .setScriptParams(temParams)
                .setRequest(new SearchRequest().indices(params[0]).types(params[1]))
                .get()
                .getResponse();
        Long total=sr.getHits().getTotalHits();
        if(total==0){
            return null;
        }
        if(mtotal!=null){
            mtotal.put("total",total);
        }
        SearchHit[] hits= sr.getHits().getHits();
        List<T> list=new ArrayList<>();
        for(SearchHit hit:hits){
            T t= mapper.readValue(hit.getSourceAsString(),(Class <T>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
            list.add(t);
        }
        return list;
    }

    public String getOne(String id){
        String[] params = prepareIndex();
        GetResponse response = elasticsearchTemplate.getClient().prepareGet(params[0],params[1],id).get();
        String s= response.getSourceAsString();
        return s;
    }

    public T getOneEntity(String id){
        String[] params = prepareIndex() ;
        GetResponse response = elasticsearchTemplate.getClient().prepareGet(params[0],params[1],id).get();
        String s= response.getSourceAsString();
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
        List<T> ts=new ArrayList<>();
        MultiGetResponse multiGetItemResponses = elasticsearchTemplate.getClient().prepareMultiGet().add(params[0],params[1],ids).get();
        for (MultiGetItemResponse itemResponse : multiGetItemResponses) {
            GetResponse response = itemResponse.getResponse();
            if (response.isExists()) {
                try {
                    T t= mapper.readValue(response.getSourceAsBytes(),(Class <T>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
                    ts.add(t);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ts;
    }

    public boolean save(T t) {
        byte[] json =null;
        try {
            json = mapper.writeValueAsBytes(t);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String[] params = prepareIndex(t);
        IndexResponse response= elasticsearchTemplate.getClient().prepareIndex(params[0],params[1],params[2]).setSource(json,XContentType.JSON).get();
        String result= response.getResult().getLowercase();
        if(result.equals("created")){
            return true;
        }
        return false;
    }

    public boolean update(T t) {
        String[] params = prepareIndex(t);
        byte[] json =null;
        try {
            json = mapper.writeValueAsBytes(t);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        UpdateRequest updateRequest = new UpdateRequest(params[0], params[1],params[2]).doc(json,XContentType.JSON);
        UpdateResponse response= null;
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
        String result= response.getResult().getLowercase();
        if(result.equals("deleted")){
            return true;
        }
        return false;
    }

    public List<Object> getFieldValue(String script,Map<String,Object> param) throws IOException {
        String[] params= prepareIndex();
        SearchResponse sr = new SearchTemplateRequestBuilder(elasticsearchTemplate.getClient())
                .setScript(script)
                .setScriptType(ScriptType.STORED)
                .setScriptParams(param)
                .setRequest(new SearchRequest().indices(params[0]).types(params[1]))
                .get()
                .getResponse();
        String value =sr.toString();
        List<Object> objs= sr.getHits().getHits()[0].getFields().get("start").getValues();
        return objs;
    }

    public Long getAggValue(String script,Map<String,Object> param) throws IOException {
        String[] params= prepareIndex();
        SearchResponse sr = new SearchTemplateRequestBuilder(elasticsearchTemplate.getClient())
                .setScript(script)
                .setScriptType(ScriptType.STORED)
                .setScriptParams(param)
                .setRequest(new SearchRequest().indices(params[0]).types(params[1]))
                .get()
                .getResponse();
        String value =sr.toString();
        Map<String,Object> map= mapper.readValue(value,new TypeReference<HashMap<String, Object>>() {});
        Map<String,Object> map1= (Map<String,Object>)map.get("aggregations");
        Map<String,Object> map2= (Map<String,Object>)map1.get("agg_num");
        Float value1= Float.parseFloat(map2.get("value").toString());
        return value1.longValue();
    }

    public Long getTotalValue(String script,Map<String,Object> param) throws IOException {
        String[] params= prepareIndex();
        SearchResponse sr = new SearchTemplateRequestBuilder(elasticsearchTemplate.getClient())
                .setScript(script)
                .setScriptType(ScriptType.STORED)
                .setScriptParams(param)
                .setRequest(new SearchRequest().indices(params[0]).types(params[1]))
                .get()
                .getResponse();
        Long num= sr.getHits().getTotalHits();
        return num;
    }

    public float getMaxScore(String script,Map<String,Object> param) throws IOException {
        String[] params = prepareIndex();
        SearchResponse sr = new SearchTemplateRequestBuilder(elasticsearchTemplate.getClient())
                .setScript(script)
                .setScriptType(ScriptType.INLINE)
                .setScriptParams(param)
                .setRequest(new SearchRequest().indices(params[0]).types(params[1]))
                .get()
                .getResponse();
        if(sr.getHits().getHits().length==0){
            return 0;
        }
        float score= sr.getHits().getHits()[0].getScore();
        return score;
    }

    String[] prepareIndex(T t){
        String index= t.getClass().getAnnotation(ESDocument.class).index();
        String type= t.getClass().getAnnotation(ESDocument.class).type();
        String id=null;
        Field[] fields=t.getClass().getDeclaredFields();
        for(Field f:fields){
            if(f.getAnnotation(Id.class)!=null){
                try {
                    f.setAccessible(true);
                    id=f.get(t).toString();
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
        String index= entityClass.getAnnotation(ESDocument.class).index();
        String type= entityClass.getAnnotation(ESDocument.class).type();
        return new String[]{index,type};
    }
}
