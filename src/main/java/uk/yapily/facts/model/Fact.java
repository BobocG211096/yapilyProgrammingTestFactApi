package uk.yapily.facts.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Fact {
    private String id;

    private String text;

    private String url;

    private String language;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("randomUselessFactUrl")
    public String getRandomUselessFactUrl() {
        return url;
    }

    @JsonProperty("permalink")
    public void setPermalink(String randomUselessFactUrl) {
        this.url = randomUselessFactUrl;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fact fact = (Fact) o;
        return  Objects.equals(text, fact.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, url, language);
    }
}
