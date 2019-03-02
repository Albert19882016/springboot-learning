package com.learning.search.utils;

public interface ESConstants {

     String match="{\"match\": {\"%s\":\"%s\"}}";

     String term = "{\"term\":{\"%s\":\"%s\"}}";

     String terms = "{\"terms\":{\"%s\":[%s]}}";

     String sort = "{\"%s\":{\"order\":\"%s\"}}";

     String match_all = "{\"match_all\":{}}";

     String range_lte = "{\"range\":{\"%s\":{\"lte\":%s}}}";

     String range_gte = "{\"range\":{\"%s\":{\"gt\":%s}}}";

     String wildcard="{\"wildcard\":{\"%s.keyword\":\"%s\"}}";

     String term_keyword="{\"term\": {\"%s.keyword\":\"%s\"}}";

     String exists = "{\"exists\":{\"field\":\"%s\"}}";

     String range = "{\"range\":{\"%s\":{\"gte\":%s,\"lte\":%s}}}";

     String sort_asc = "{\"%s\":{\"order\":\"asc\"}}";

     String sort_desc = "{\"%s\":{\"order\":\"desc\"}}";
}
