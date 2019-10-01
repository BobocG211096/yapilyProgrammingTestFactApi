package uk.yapily.facts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.yapily.facts.cache.FactDataCacheInMemory;
import uk.yapily.facts.exception.FactNotFound;
import uk.yapily.facts.model.Fact;

import java.util.ArrayList;
import java.util.List;

@Component
public class FactService {
    private FactDataCacheInMemory factDataCacheInMemory;

    @Autowired
    public FactService (FactDataCacheInMemory factDataCacheInMemory){
        this.factDataCacheInMemory = factDataCacheInMemory;
    }

    public List<Fact> getFacts(){
        List<Fact> facts = new ArrayList<>();
        factDataCacheInMemory.getCache().values().forEach(factCacheObject -> facts.add(factCacheObject.getFact()));

        return facts;
    }

    public Fact getFactById(String factId) throws FactNotFound {
       return factDataCacheInMemory.get(factId);
    }
}
