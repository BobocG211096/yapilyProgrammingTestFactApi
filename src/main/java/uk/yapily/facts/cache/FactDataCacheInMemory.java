package uk.yapily.facts.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.yapily.facts.exception.FactNotFound;
import uk.yapily.facts.model.Fact;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

// I choose this implementation from the https://explainjava.com/simple-in-memory-cache-java/, because I have to choose between caching into a file,
// which sounds also very good to me, but I didn't want that every time to access the file and do a O(n) search for the right resource.
// This solution I choose, because firstly the retrieval complexity is much faster and also because I can configure the removal time
// of the cached facts. Also, by saving into a map, I will not have duplicated  values in the system and only unique, because every time I will
// save only unique values because of the map implementation on the key which will override the existing value. If every time the RUF API will generate
// a new one then I will make a comparison on actual values that the map is holding. Thing not possible by using a file, because I had to access the file
// over and over again or to save into a set all the values and then introduce into a file, but then I will have to access the file again to retrieve values.
// I configure this method by removing some methods, that I believed that for my case are not applying.
@Component
public class FactDataCacheInMemory {
    @Value("${fact.cache.cleanUpPeriodInSeconds}")
    private int cleanUpPeriodInSeconds;

    @Value("${fact.cache.expiryTimePeriodInMillis}")
    private int expiryTimePeriodInMillis;

    private Map<String, FactCacheObject> cache = new ConcurrentHashMap<>();

    public FactDataCacheInMemory() {
        Thread cleanerThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(cleanUpPeriodInSeconds * 1000);
                    cache.entrySet().removeIf(entry -> Optional.ofNullable(entry.getValue()).map(FactCacheObject::isExpired).orElse(false));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        cleanerThread.setDaemon(true);
        cleanerThread.start();
    }

    public void add(Fact fact) {
        long expiryTime = System.currentTimeMillis() + expiryTimePeriodInMillis * 1000;
        FactCacheObject factCacheObject = new FactCacheObject(fact, expiryTime);

        if (!cache.containsValue(factCacheObject)) {
            cache.put(fact.getId(), factCacheObject);
        }
    }

    public Fact get(String id) throws FactNotFound {
        return Optional.ofNullable(cache.get(id)).filter(cacheObject -> !cacheObject.isExpired()).map(FactCacheObject::getFact).orElseThrow(FactNotFound::new);
    }

    public long size() {
        return cache.entrySet().stream().filter(entry -> Optional.ofNullable(entry.getValue()).map(cacheObject -> !cacheObject.isExpired()).orElse(false)).count();
    }

    public Map<String, FactCacheObject> getCache() {
        return cache;
    }

    public static class FactCacheObject {
        private Fact fact;
        private long expiryTime;

        public FactCacheObject(Fact fact, long expiryTime) {
            this.fact = fact;
            this.expiryTime = expiryTime;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }

        public Fact getFact() {
            return fact;
        }

        public long getExpiryTime() {
            return expiryTime;
        }

        public void updateExpiryTime(long newExpiryTime) {
            expiryTime = newExpiryTime;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FactCacheObject that = (FactCacheObject) o;
            return fact.equals(that.fact);
        }

        @Override
        public int hashCode() {
            return fact.hashCode();
        }
    }
}

