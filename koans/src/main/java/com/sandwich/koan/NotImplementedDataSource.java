package com.sandwich.koan;

import com.ericsson.cifwk.taf.annotations.DataSource;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class NotImplementedDataSource {

    @DataSource
    public Iterable<Map<String,Object>> noImplementationDataSource(){
        List<Map<String, Object>> list = Lists.newArrayList();
        Map<String, Object> map = Maps.newHashMap();
        map.put("","");
        list.add(map);
        return list;
    }
}
