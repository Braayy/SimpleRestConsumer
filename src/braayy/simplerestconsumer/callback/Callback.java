package braayy.simplerestconsumer.callback;

import com.google.gson.JsonObject;

@FunctionalInterface
public interface Callback<T> {

    void call(Exception err, T response);

}