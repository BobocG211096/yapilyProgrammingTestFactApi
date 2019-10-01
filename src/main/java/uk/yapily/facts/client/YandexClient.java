package uk.yapily.facts.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.nonNull;

//I know that the code is duplicated from the RandomUselessFactsClient and I could unite them into a single one class named
//HttpClient, but for better visibility and for better clean code I prefer to know which client I am making the request
@Component
public class YandexClient {
    public enum HttpMethod {
        GET
    }

    public String sendRequest(URL url,
                              RandomUselessFactsClient.HttpMethod method) throws IOException {
        final HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        try {
            connection.setRequestMethod(method.name());
            connection.setRequestProperty("Content-Type", "application/xml");

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
