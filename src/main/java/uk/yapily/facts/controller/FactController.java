package uk.yapily.facts.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;
import uk.yapily.facts.exception.FactNotFound;
import uk.yapily.facts.exception.UnsupportedLanguageException;
import uk.yapily.facts.model.Fact;
import uk.yapily.facts.service.FactService;
import uk.yapily.facts.service.FactTranslateService;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

import static java.util.Objects.isNull;

@RequestMapping("/facts")
@RestController
public class FactController {
    private FactService factService;
    private FactTranslateService factTranslateService;

    @Autowired
    public FactController(FactService factService,
                          FactTranslateService factTranslateService){
        this.factService = factService;
        this.factTranslateService = factTranslateService;
    }

    @ApiOperation("Get the list of cached facts")
    @GetMapping
    public List<Fact> getFacts(){
        return factService.getFacts();
    }

    @ApiOperation("Get a cached fact by id and optional can translate the text of the fact to a different language")
    @GetMapping("/{factId}")
    public Fact getFactById(@PathVariable String factId,
                            @RequestParam(name = "lang", required = false) String languageCode) throws IOException, UnsupportedLanguageException, FactNotFound, ParserConfigurationException, SAXException {

        Fact fact = factService.getFactById(factId);
        if(isNull(languageCode)) {
            return fact;
        }

        return factTranslateService.translateFactTextTo(fact, languageCode);
    }

}
