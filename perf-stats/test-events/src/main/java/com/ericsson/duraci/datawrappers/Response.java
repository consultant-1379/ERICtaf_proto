package com.ericsson.duraci.datawrappers;

public class Response {

    ResultCode result;
    String type;
    String data;
    Integer size;
    Integer time;

    public Response() {
    }

    public Response(ResultCode result, String type, String data, Integer size, Integer time) {
        this.result = result;
        this.type = type;
        this.data = data;
        this.size = size;
        this.time = time;
    }

    public ResultCode getResult() {
        return result;
    }

    public void setResult(ResultCode result) {
        this.result = result;
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

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }
}
