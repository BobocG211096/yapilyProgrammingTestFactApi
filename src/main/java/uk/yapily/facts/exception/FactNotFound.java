package uk.yapily.facts.exception;

public class FactNotFound extends Exception {
    public FactNotFound() {
        super("Fact was not found!");
    }
}
