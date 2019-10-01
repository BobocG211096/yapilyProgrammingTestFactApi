package uk.yapily.facts.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.nonNull;


@Component
public class RandomUselessFactsClient {

    //In case that RUF Api will provide other than GET http method and we will wan† to integrate into our application
    public enum HttpMethod {
        GET
    }

    //As Random Useless Fact Api serves more endpoint to get the useless fact I made it more addaptable to take use of the both
    public String sendRequest(URL url,
                             HttpMethod method) throws IOException {
        final HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        try {
            connection.setRequestMethod(method.name());
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            return getResponse(connection);
        } finally {
            connection.disconnect();
        }
    }

    private String getResponse(HttpsURLConnection connection) throws IOException {
        try (InputStream is = connection.getInputStream()) {
            String body = new String(is.readAllBytes(), UTF_8);
            return body;
        } catch (JsonProcessingException e) {
            throw e;
        } catch (IOException e) {
            try (InputStream is = connection.getErrorStream()) {
                if (nonNull(is)) {
                    throw new RuntimeException(connection.getResponseMessage());
                } else {
                    throw e;
                }
            }
        }
    }

}
