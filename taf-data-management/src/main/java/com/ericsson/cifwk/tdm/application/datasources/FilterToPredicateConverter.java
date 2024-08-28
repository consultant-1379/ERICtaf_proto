package com.ericsson.cifwk.tdm.application.datasources;

import com.ericsson.cifwk.tdm.api.RecordPredicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 19/02/2016
 */
@Service
public class FilterToPredicateConverter {

    private final Map<String, String> operations;

    //TODO: look at MVEL predicates
    public FilterToPredicateConverter() {
        operations = Maps.newLinkedHashMap();
        //order is important
        operations.put("!=", "$ne");
        operations.put(">=", "$gte");
        operations.put("<=", "$lte");
        operations.put("=", "$eg");
        operations.put(">", "$gt");
        operations.put("<", "$lt");
    }

    public List<RecordPredicate> convert(List<String> predicates) {
        if (predicates == null) {
            return Lists.newArrayList();
        }
        return predicates.stream().map(this::parse)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }


    private Optional<RecordPredicate> parse(String predicateString) {
        for (String operator : operations.keySet()) {
            if (predicateString.contains(operator)) {
                String[] split = predicateString.split(operator);
                if (split.length == 2) {
                    String mongoDbOperator = operations.get(operator);
                    return Optional.of(new RecordPredicate(split[0], mongoDbOperator, split[1]));
                } else {
                    return Optional.empty();
                }
            }
        }
        return Optional.empty();
    }
}
