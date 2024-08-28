package com.ericsson.cifwk.taf.dashboard.infrastructure.mapping;

import com.ericsson.cifwk.taf.dashboard.api.dto.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by egergle on 04/01/2016.
 */
public class NodeServicesMapper implements NodeMapper<List<Node>, String> {

    public static final String CLUSTER_INDEX_CHAR = "_";
    public static final String TABLE_INDEX_SPLITTER = "\\s+";

    @Override
    public List<Node> map(String dto) {
        String[] info = dto.split("\n");
        List<Node> nodes = new ArrayList();

        if (info.length > 5) {
            info = Arrays.copyOfRange(info, 5, info.length - 1);

            for (String item : info) {
                if (item.contains("----")) {
                    continue;
                }
                Node node = new Node();
                String[] columns = item.trim().split(TABLE_INDEX_SPLITTER);
                node.setType(columns[0]);
                node.setName(getClusterName(columns[1], columns[0]));
                node.setGroup(columns[1]);
                node.setSystem(columns[2]);
                node.setHaType(columns[3]);
                node.setStatus(columns[5]);
                nodes.add(node);
            }
        }

        return nodes;
    }

    private String getClusterName(String name, String matcherChar) {
        int index = name.lastIndexOf(matcherChar + CLUSTER_INDEX_CHAR);
        return name.substring(index + (1 + matcherChar.length()));
    }

}
