package uk.yapily.facts;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import uk.yapily.facts.cache.FactDataCacheInMemory;
import uk.yapily.facts.client.RandomUselessFactsClient;
import uk.yapily.facts.model.Fact;
import uk.yapily.facts.model.Status;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static uk.yapily.facts.client.RandomUselessFactsClient.HttpMethod.GET;

@Component
public class RandomUselessFactClientRunner implements ApplicationRunner {
    private RandomUselessFactsClient randomUselessFactsClient;
    private FactDataCacheInMemory factDataCacheInMemory;
    private ObjectMapper objectMapper;

    private String numberOfRandomUselessFacts;
    private String baseUrl;

    public static Status statusLoading = Status.LOADING_DATA;

    @Autowired
    public RandomUselessFactClientRunner(RandomUselessFactsClient randomUselessFactsClient,
                                         FactDataCacheInMemory factDataCacheInMemory,
                                         ObjectMapper objectMapper,
                                         @Value("${useless.facts.client.numberOfRandomUselessFacts}") String numberOfRandomUselessFacts,
                                         @Value("${useless.facts.client.baseUrl}") String baseUrl) {
        this.randomUselessFactsClient = randomUselessFactsClient;
        this.factDataCacheInMemory = factDataCacheInMemory;
        this.objectMapper = objectMapper;
        this.baseUrl = baseUrl;
        this.numberOfRandomUselessFacts = numberOfRandomUselessFacts;
    }

    @Override
    public void run(ApplicationArguments args) throws MalformedURLException {
        URL randomUselessFactsUrl = new URL(baseUrl + "/random.json?language=en");
        for (int factNumber = 0; factNumber < Integer.parseInt(numberOfRandomUselessFacts); factNumber++) {
            Fact fact = null;
            try {
                fact = getRandomUselessFact(randomUselessFactsUrl);
            } catch (IOException e) {
                statusLoading = Status.ERROR;
            }

            factDataCacheInMemory.add(fact);
        }

        statusLoading = Status.COMPLETED;
    }

    private Fact getRandomUselessFact(URL randomUselessFactsUrl) throws IOException {
        String stringRandomUselessFact = randomUselessFactsClient.sendRequest(randomUselessFactsUrl, GET);
        return objectMapper.readValue(stringRandomUselessFact, Fact.class);
    }
}
