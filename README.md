# Simple Rest Consumer
A simple rest consumer for java

## Usage
```java
// Initializate the consumer, put the base url on the parameter.
RestConsumer consumer = new RestConsumer("http://jsonplaceholder.typicode.com/");

// Here comes a example json body.
JsonObject body = new JsonObject();
body.addProperty("title", "Example");
body.addProperty("body", "Just an example");
body.addProperty("userId", 2);

// Here comes the request configuration.
Request request = Request.builder()
    .endpoint("posts")
    .body(body)
    .timeout(2000)
    .build();

// Here it will consume the rest api
// err is a Exception in cause something goes wrong and response is the of type <T> that represents the response of the api
consumer.<JsonObject>consume(request, (err, response) -> {
    if (err != null) {
        err.printStackTrace();
        return;
    }

    System.out.println(RestConsumer.GSON.toJson(response));
});

consumer.close();
```
