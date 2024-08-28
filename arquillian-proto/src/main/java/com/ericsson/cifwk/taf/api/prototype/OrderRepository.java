package com.ericsson.cifwk.taf.api.prototype;

import java.util.List;
import javax.ejb.Local;

@Local
public interface OrderRepository {
    void addOrder(List<String> order);
    List<List<String>> getOrders();
    int getOrderCount();
}
