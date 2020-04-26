package braayy.simplerestconsumer.api;

import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

public class Request {

    private final String endpoint;
    private final Method method;
    private final JsonElement body;
    private final Map<String, String> headers;
    private final int timeout;

    Request(String endpoint, Method method, JsonElement body, Map<String, String> headers, int timeout) {
        this.endpoint = endpoint;
        this.method = method;
        this.body = body;
        this.headers = headers;
        this.timeout = timeout;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public Method getMethod() {
        return method;
    }

    public JsonElement getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public static Builder builder() {
        return new Builder();
    }

    public int getTimeout() {
        return timeout;
    }

    public enum Method {
        GET, POST, PUT, DELETE;
    }

    public static class Builder {
        private String endpoint;
        private Method method;
        private JsonElement body;
        private Map<String, String> headers;
        private int timeout;

        Builder() {
            this.headers = new HashMap<>();
        }

        public Builder endpoint(String endpoint) {
            this.endpoint = endpoint;

            return this;
        }

        public Builder method(Method method) {
            this.method = method;

            return this;
        }

        public Builder body(JsonElement body) {
            this.body = body;

            return this;
        }

        public Builder header(String header, String value) {
            this.headers.put(header, value);

            return this;
        }

        public Builder timeout(int timeout) {
            this.timeout = timeout;

            return this;
        }

        public Request build() {
            if (this.endpoint == null) throw new IllegalArgumentException("endpoint can not be null");
            if (this.method == null) this.method = Method.GET;

            return new Request(this.endpoint, this.method, this.body, this.headers, this.timeout);
        }
    }

}