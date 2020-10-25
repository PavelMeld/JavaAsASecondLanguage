package io.github.javaasasecondlanguage.flitter.utils;

import java.util.Map;

import static java.lang.String.format;

public class RequestInfo {
    private String endpoint;
    private Method method;
    private Map<String, String> params;
    private Object response;

    public RequestInfo() {
    }

    public RequestInfo(String endpoint, Method method,
                       Map<String, String> params, Object response) {
        this.endpoint = endpoint;
        this.method = method;
        this.params = params;
        this.response = response;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return format(
                "Full info about last request: endpoint='%s', method=%s, params=%s, response=%s}",
                endpoint,
                method,
                params,
                response
        );
    }

    enum Method {
        GET, POST, DELETE
    }
}
