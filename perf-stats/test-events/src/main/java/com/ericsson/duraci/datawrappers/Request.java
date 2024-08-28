package com.ericsson.duraci.datawrappers;

public class Request {

    String target;
    String type;
    String data;
    Integer size;

    public Request() {
    }

    public Request(String target, String type, String data, Integer size) {
        this.target = target;
        this.type = type;
        this.data = data;
        this.size = size;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
