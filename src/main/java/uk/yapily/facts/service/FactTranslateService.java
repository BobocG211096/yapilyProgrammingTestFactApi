package uk.yapily.facts.service;

import com.neovisionaries.i18n.LanguageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;
import uk.yapily.facts.client.RandomUselessFactsClient;
import uk.yapily.facts.client.YandexClient;
import uk.yapily.facts.exception.UnsupportedLanguageException;
import uk.yapily.facts.model.Fact;
import uk.yapily.facts.parser.XmlParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.neovisionaries.i18n.LanguageCode.getByCode;
import static java.util.Objects.isNull;

@Component
public class FactTranslateService {
    private YandexClient yandexClient;
    private String baseUrl;
    private String apiKey;

    @Autowired
    public FactTranslateService(YandexClient yandexClient,
                                @Value("${yandex.translate.baseUrl}") String baseUrl,
                                @Value("${yandex.translate.apiKey}") String apiKey){
        this.yandexClient = yandexClient;
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    public Fact translateFactTextTo(Fact fact, String languageCode) throws UnsupportedLanguageException, IOException, SAXException, ParserConfigurationException {
        LanguageCode language = getByCode(languageCode);

        if(isNull(language)){
            throw new UnsupportedLanguageException();
        }

        URL yandexUrl = new URL(baseUrl + "?key=" + apiKey + "&text=" + URLEncoder.encode(fact.getText(), StandardCharsets.UTF_8.toString()) + "&lang=en-" + languageCode);
        updateFact(fact, languageCode, yandexUrl);

        return fact;
    }

    private void updateFact(Fact fact, String languageCode, URL yandexUrl) throws ParserConfigurationException, IOException, SAXException {
        String translatedTextXml = yandexClient.sendRequest(yandexUrl, RandomUselessFactsClient.HttpMethod.GET);
        String textTranslatedValue = XmlParser.getXmlValueFromTag(translatedTextXml, "text");

        fact.setText(textTranslatedValue);
        fact.setLanguage(languageCode);
    }
}
