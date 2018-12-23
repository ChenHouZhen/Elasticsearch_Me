package com.chenhz.transportclientelasticsearch.utils;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author CHZ
 * @create 2018/12/22
 */
@Component
@Slf4j
public class EsQueryUtils {

    @Autowired
    private TransportClient client;

    @Autowired
    private EsIndexUtils esIndexUtils;


    /**
     * 添加数据，指定ID
     *
     * 重复ID,后者会覆盖前者
     */
    public String addData(Map<String,Object> data,String index ,String type ,String id){
        IndexResponse indexResponse = client
                .prepareIndex(index,type,id)
                .setSource(data)
                .get();
        log.info("插入数据：Index:[{}],Type:[{}]+ ID:[{}]+ Version:[{}]",indexResponse.getIndex(),indexResponse.getType(),indexResponse.getId(),indexResponse.getVersion());
        return indexResponse.getId();
    }

    /**
     * 数据添加 ,UUID 生成ID
     *
     * @param data 要增加的数据
     * @param index      索引，类似数据库
     * @param type       类型，类似表
     */
    public String addData(Map<String,Object> data, String index, String type){
        return this.addData(data,index,type,UUIDGenerate.create());
    }

    /**
     * 通过ID删除数据
     *
     * @param index 索引，类似数据库
     * @param type  类型，类似表
     * @param id    数据ID
     */
    public void deleteDataById(String index,String type,String id){
        if (StringUtils.isEmpty(id)){
            return;
        }
        log.info("delete Elasticsearch data . index >>> [{}],type >>> [{}],id >>> [{}]",index,type,id);
        DeleteResponse response = client
                .prepareDelete(index,type,id)
                .execute()
                .actionGet();

        log.info("deleteDataById response status:[{}],id:[{}]", response.status().getStatus(), response.getId());

    }
    /**
     * 通过ID 更新数据
     *
     * @param data       要修改的数据
     * @param index      索引，类似数据库
     * @param type       类型，类似表
     * @param id         数据ID
     * @return
     */
    public void updateDataById(String index ,String type ,Map<String,Object> data, String id){
        indexCheck(index);
        if (StringUtils.isEmpty(data) || StringUtils.isEmpty(id)){
            return;
        }
        log.info("update Elasticsearch data . index >>> {[]},type >>> {[]},id >>> {[]},data >>>{[]}",index,type,id,data.toString());
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(index)
                .type(type)
                .id(id)
                .doc(data);
        // 更改立即生效
        //todo:为哈没有立即生效
//        updateRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        client.update(updateRequest);

    }


    private void indexCheck(String index){
        if (!esIndexUtils.isExistIndex(index)){
            throw new IllegalArgumentException("索引 >>> ["+index+"] 不存在 ! 请先联系管理员先创建索引");
        }
    }

    public Map<String,Object> searchDataById(String index ,String type ,String id){
        indexCheck(index);
        GetRequestBuilder getRequestBuilder = client
                .prepareGet(index,type,id);
        GetResponse getResponse = getRequestBuilder.execute().actionGet();
        Map<String,Object> result = getResponse.getSource();
        if (null == result){
            return new HashMap<>(0);
        }
        result.put("id",getResponse.getId());
        return result;
    }



    /**
     * 使用分词查询
     *
     * @param index    索引名称
     * @param type     类型名称,可传入多个type逗号分隔
     * @param size     文档大小限制
     * @param fields   需要显示的字段，逗号分隔（缺省为全部字段）
     * @param matchStr 过滤条件（xxx=111,aaa=222）
     */
    public List<Map<String, Object>> listData(String index, String type, Integer size, String fields, String matchStr) {
        return this.listData(index, type, 0l, 0l, size, fields, null, false, null, matchStr);
    }


    /**
     * 使用分词查询
     *
     * @param index    索引名称
     * @param type     类型名称,可传入多个type逗号分隔
     * @param matchStr 过滤条件（xxx=111,aaa=222）
     */
    public List<Map<String,Object>> listData(String index , String type , String matchStr){
        return this.listData(index, type, 0l, 0l, null, null, null, true, null, matchStr);
    }


    /**
     * 使用分词查询
     *
     * @param index          索引名称
     * @param type           类型名称,可传入多个type逗号分隔
     * @param startTime      开始时间
     * @param endTime        结束时间
     * @param size           文档大小限制
     * @param fields         需要显示的字段，逗号分隔（缺省为全部字段）
     * @param sortField      排序字段
     * @param matchPhrase    true 使用，短语精准匹配
     * @param highlightFields 高亮字段
     * @param matchStr       过滤条件（xxx=111,aaa=222）
     */
    public List<Map<String,Object>> listData(
            String index
            ,String type
            ,Long startTime
            ,Long endTime
            ,Integer size
            ,String fields
            ,String sortField
            ,Boolean matchPhrase
            ,String highlightFields
            ,String matchStr
    ){
        indexCheck(index);
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        if (!StringUtils.isEmpty(type)){
            searchRequestBuilder.setTypes(type.split(","));
        }

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (startTime > 0 && endTime > 0){
            boolQueryBuilder.must(QueryBuilders.rangeQuery("timestamp")
                    .format("epoch_millis")
                    .from(startTime)
                    .to(endTime)
                    .includeLower(true)
                    .includeUpper(true));
        }

        // 搜索的字段
        if (!StringUtils.isEmpty(matchStr)){
            for (String s: matchStr.split(",")) {
                String[] ss = s.split("=");
                if (ss.length > 1){
                    if (Boolean.TRUE.equals(matchPhrase)){
                        boolQueryBuilder.must(
                                QueryBuilders.matchPhraseQuery(s.split("=")[0],s.split("=")[1])
                        );
                    } else {
                        boolQueryBuilder.must(
                                QueryBuilders.matchQuery(s.split("=")[0],s.split("=")[1])
                        );
                    }
                }
            }
        }
        // 高亮 （xxx=111,aaa=222）
        if (!StringUtils.isEmpty(highlightFields)){
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            //highlightBuilder.preTags("<span style='color:red' >");//设置前缀
            //highlightBuilder.postTags("</span>");//设置后缀

            // 设置高亮字段
            highlightBuilder.field(highlightFields);
            searchRequestBuilder.highlighter(highlightBuilder);
        }
        searchRequestBuilder.setQuery(boolQueryBuilder);

        // 返回字段
        if (!StringUtils.isEmpty(fields)){
            searchRequestBuilder.setFetchSource(fields.split(","),null);
        }
        searchRequestBuilder.setFetchSource(true);

        // 排序 降序
        if (!StringUtils.isEmpty(sortField)){
            searchRequestBuilder.addSort(sortField, SortOrder.DESC);
        }

        // 文档数量限制
        if (size != null && size > 0){
            searchRequestBuilder.setSize(size);
        }
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        long totalHits = searchResponse.getHits().totalHits;
        long length = searchResponse.getHits().getHits().length;
        log.info("共查询到[{}]条数据,处理数据条数[{}],查询条件为：[{}]", totalHits, length,boolQueryBuilder.toString());

        if (searchResponse.status().getStatus() == 200){
            return setSearchResponse(searchResponse,highlightFields);
        }
        return new ArrayList<>();
    }


    public PageUtils pageData(String index
            ,String type
            ,String sortField
            ,Boolean matchPhrase
            ,String matchStr
            ,Integer currentPage
            ,Integer pageSize){
        return this.pageData(index,type,0l,0l,null,sortField,matchPhrase,null,matchStr,currentPage,pageSize);
    }


    /**
     * 使用分词查询,并分页
     *
     * @param index          索引名称
     * @param type           类型名称,可传入多个type逗号分隔
     * @param currentPage    当前页
     * @param pageSize       每页显示条数
     * @param startTime      开始时间
     * @param endTime        结束时间
     * @param fields         需要显示的字段，逗号分隔（缺省为全部字段）
     * @param sortField      排序字段
     * @param matchPhrase    true 使用，短语精准匹配
     * @param highlightFields 高亮字段
     * @param matchStr       过滤条件（xxx=111,aaa=222）
     * @return
     */
    public PageUtils pageData(
            String index
            ,String type
            ,Long startTime
            ,Long endTime
            ,String fields
            ,String sortField
            ,Boolean matchPhrase
            ,String highlightFields
            ,String matchStr
            ,Integer currentPage
            ,Integer pageSize){
        indexCheck(index);
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index);
        if (!StringUtils.isEmpty(type)) {
            searchRequestBuilder.setTypes(type.split(","));
        }
        searchRequestBuilder.setSearchType(SearchType.QUERY_THEN_FETCH);
        // 需要显示的字段，逗号分隔（缺省为全部字段）
        if (!StringUtils.isEmpty(fields)) {
            searchRequestBuilder.setFetchSource(fields.split(","), null);
        }
        //排序字段 降序
        if (!StringUtils.isEmpty(sortField)) {
            searchRequestBuilder.addSort(sortField, SortOrder.DESC);
        }
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (startTime > 0 && endTime > 0) {
            boolQuery.must(QueryBuilders.rangeQuery("timestamp")
                    .format("epoch_millis")
                    .from(startTime)
                    .to(endTime)
                    .includeLower(true)
                    .includeUpper(true));
        }
        // 查询字段
        if (!StringUtils.isEmpty(matchStr)) {
            for (String s : matchStr.split(",")) {
                String[] ss = s.split("=");
                if (Boolean.TRUE.equals(matchPhrase)) {
                    boolQuery.must(QueryBuilders.matchPhraseQuery(s.split("=")[0], s.split("=")[1]));
                } else {
                    boolQuery.must(QueryBuilders.matchQuery(s.split("=")[0], s.split("=")[1]));
                }
            }
        }
        // 高亮（xxx=111,aaa=222）
        if (!StringUtils.isEmpty(highlightFields)) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();

            //highlightBuilder.preTags("<span style='color:red' >");//设置前缀
            //highlightBuilder.postTags("</span>");//设置后缀

            // 设置高亮字段
            highlightBuilder.field(highlightFields);
            searchRequestBuilder.highlighter(highlightBuilder);
        }

        searchRequestBuilder.setQuery(QueryBuilders.matchAllQuery());
        searchRequestBuilder.setQuery(boolQuery);
        // 分页应用
        searchRequestBuilder.setFrom(currentPage).setSize(pageSize);
        // 设置是否按查询匹配度排序
        searchRequestBuilder.setExplain(true);
        // 执行搜索,返回搜索响应信息
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        long totalHits = searchResponse.getHits().totalHits;
        long length = searchResponse.getHits().getHits().length;
        log.info("共查询到[{}]条数据,处理数据条数[{}],查询条件为：[{}]", totalHits, length,boolQuery.toString());
        if (searchResponse.status().getStatus() == 200) {
            // 解析对象
            List<Map<String, Object>> sourceList = setSearchResponse(searchResponse, highlightFields);
            PageUtils pageUtils = new PageUtils(sourceList,(int)totalHits,pageSize,currentPage);
            return pageUtils;
        }

        return null;
    }


    /**
     * 高亮结果集 特殊处理
     */
    private List<Map<String,Object>> setSearchResponse(SearchResponse searchResponse,String highlightFiels){
        List<Map<String,Object>> sourceList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (SearchHit searchHit : searchResponse.getHits().getHits()){
            searchHit.getSourceAsMap().put("id",searchHit.getId());
            if (!StringUtils.isEmpty(highlightFiels)){
                log.info("遍历 高亮结果集，覆盖 正常结果集");
                Text[] texts = searchHit.getHighlightFields().get(highlightFiels)
                        .getFragments();
                if (texts != null){
                    for (Text str: texts) {
                        sb.append(str);
                    }
                    searchHit.getSourceAsMap().put(highlightFiels,sb.toString());
                }
            }
            sourceList.add(searchHit.getSourceAsMap());
        }
        return sourceList;
    }
}
