package braayy.simplerestconsumer;

import braayy.simplerestconsumer.api.Request;
import com.google.gson.JsonObject;

public class Example {

    public static void main(String[] args) {
        RestConsumer consumer = new RestConsumer("http://jsonplaceholder.typicode.com/");

        JsonObject body = new JsonObject();
        body.addProperty("title", "Example");
        body.addProperty("body", "Just an example");
        body.addProperty("userId", 2);

        Request request = Request.builder()
                .endpoint("posts")
                .body(body)
                .timeout(2000)
                .build();

        consumer.<JsonObject>consume(request, (err, response) -> {
            if (err != null) {
                err.printStackTrace();
                return;
            }


        });

        consumer.close();
    }

}