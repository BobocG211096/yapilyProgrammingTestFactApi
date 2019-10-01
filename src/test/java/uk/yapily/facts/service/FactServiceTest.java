package uk.yapily.facts.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import uk.yapily.facts.cache.FactDataCacheInMemory;
import uk.yapily.facts.exception.FactNotFound;
import uk.yapily.facts.model.Fact;
import utils.TestUtils;

import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FactServiceTest {
    @Mock
    private FactDataCacheInMemory factDataCacheInMemory;

    @Autowired
    @InjectMocks
    private FactService factService;

    @Test
    public void getFacts_Successfully() {
        when(factDataCacheInMemory.getCache()).thenReturn(TestUtils.createCache());

        List<Fact> facts = factService.getFacts();

        Assert.assertFalse(facts.isEmpty());
        Assert.assertEquals(2, facts.size());

        Assert.assertEquals("some-key-1", facts.get(1).getId());
        Assert.assertEquals("some-text", facts.get(1).getText());
        Assert.assertEquals("some-language", facts.get(1).getLanguage());
        Assert.assertEquals("some-randomUselessFactUrl", facts.get(1).getRandomUselessFactUrl());

        Assert.assertEquals("some-key-2", facts.get(0).getId());
        Assert.assertEquals("some-text", facts.get(0).getText());
        Assert.assertEquals("some-language", facts.get(0).getLanguage());
        Assert.assertEquals("some-randomUselessFactUrl", facts.get(0).getRandomUselessFactUrl());
    }

    @Test
    public void getFactById_Successfully() throws FactNotFound {
        when(factDataCacheInMemory.get(any(String.class))).thenReturn(TestUtils.createFacts().get(0));

        Fact factById = factService.getFactById("some-key-1");

        Assert.assertTrue(Objects.nonNull(factById));

        Assert.assertEquals("some-key-1", factById.getId());
        Assert.assertEquals("some-text", factById.getText());
        Assert.assertEquals("some-language", factById.getLanguage());
        Assert.assertEquals("some-randomUselessFactUrl", factById.getRandomUselessFactUrl());
    }
}