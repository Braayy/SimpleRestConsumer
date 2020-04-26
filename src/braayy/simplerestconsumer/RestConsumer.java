package braayy.simplerestconsumer;

import braayy.simplerestconsumer.api.Request;
import braayy.simplerestconsumer.callback.Callback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RestConsumer implements AutoCloseable {

    private final Gson GSON = new GsonBuilder()
                                .setPrettyPrinting()
                                .create();

    private final String baseUrl;
    private final ExecutorService executor;

    public RestConsumer(String baseUrl) {
        this.baseUrl = baseUrl;

        this.executor = Executors.newCachedThreadPool();
    }

    @SuppressWarnings("unchecked")
    public <T extends JsonElement> void consume(Request request, Callback<T> callback) {
        this.executor.submit(() -> {
            try {
                URL url = new URL(this.baseUrl + normalizeEndpoint(request.getEndpoint()));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(request.getMethod().name());
                connection.setDoOutput(true);
                connection.setConnectTimeout(request.getTimeout());
                connection.setReadTimeout(request.getTimeout());

                for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }

                if (request.getBody() != null) {
                    JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(connection.getOutputStream()));

                    GSON.toJson(request.getBody(), jsonWriter);
                    jsonWriter.close();
                }

                connection.connect();

                JsonReader jsonReader = new JsonReader(new InputStreamReader(connection.getInputStream()));
                JsonElement response = GSON.fromJson(jsonReader, JsonElement.class);

                callback.call(null, (T) response);
            } catch (Exception ex) {
                callback.call(ex, null);
            }
        });
    }

    private static String normalizeEndpoint(String endpoint) {
        return endpoint.startsWith("/") ? endpoint.substring(1) : endpoint;
    }

    @Override
    public void close() {
        this.executor.shutdownNow();
    }
}
