package utils;

import uk.yapily.facts.cache.FactDataCacheInMemory;
import uk.yapily.facts.model.Fact;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TestUtils {
    public static Map<String, FactDataCacheInMemory.FactCacheObject> createCache(){
        Map<String, FactDataCacheInMemory.FactCacheObject> cache = new ConcurrentHashMap<>();

        List<Fact> facts = createFacts();

        cache.put("some-key-1", new FactDataCacheInMemory.FactCacheObject(facts.get(0), 0));
        cache.put("some-key-2", new FactDataCacheInMemory.FactCacheObject(facts.get(1), 0));

        return cache;
    }

    public static List<Fact> createFacts(){
        List<Fact> facts = new ArrayList<>();
        Fact fact = new Fact();

        fact.setId("some-key-1");
        fact.setText("some-text");
        fact.setLanguage("some-language");
        fact.setPermalink("some-randomUselessFactUrl");

        facts.add(fact);

        Fact anotherFact = new Fact();

        anotherFact.setId("some-key-2");
        anotherFact.setText("some-text");
        anotherFact.setLanguage("some-language");
        anotherFact.setPermalink("some-randomUselessFactUrl");

        facts.add(anotherFact);

        return facts;
    }
}
