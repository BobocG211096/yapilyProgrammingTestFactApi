package uk.yapily.facts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.yapily.facts.RandomUselessFactClientRunner;
import uk.yapily.facts.cache.FactDataCacheInMemory;
import uk.yapily.facts.model.StatusFact;
import uk.yapily.facts.model.StatusFactResponse;

@Component
public class StatusFactService {
    private FactDataCacheInMemory factDataCacheInMemory;

    private int numberOfRandomUselessFacts;

    @Autowired
    public StatusFactService(FactDataCacheInMemory factDataCacheInMemory,
            @Value("${useless.facts.client.numberOfRandomUselessFacts}")int numberOfRandomUselessFacts) {
        this.factDataCacheInMemory = factDataCacheInMemory;
        this.numberOfRandomUselessFacts = numberOfRandomUselessFacts;
    }

    public StatusFactResponse retrieveStatusFactResponse(){
       return createStatusFactResponse();
    }

    private StatusFactResponse createStatusFactResponse(){
        StatusFactResponse statusFactResponse = new StatusFactResponse();

        statusFactResponse.setStatus(RandomUselessFactClientRunner.statusLoading);

        StatusFact statusFact = createStatusFact();
        statusFactResponse.setStatusFact(statusFact);

        return statusFactResponse;
    }

    private StatusFact createStatusFact(){
        StatusFact statusFact = new StatusFact();

        statusFact.setTotalFacts(numberOfRandomUselessFacts);
        statusFact.setUniqueFacts(factDataCacheInMemory.size());

        return statusFact;
    }


}
