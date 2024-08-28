package com.ericsson.cifwk.taf.dashboard.infrastructure.mapping;

/**
 * Created by egergle on 04/01/2016.
 */
public interface NodeMapper<T, K> {
    T map(K dto);
}
