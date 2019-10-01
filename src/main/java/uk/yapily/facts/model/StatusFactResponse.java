package uk.yapily.facts.model;

public class StatusFactResponse {
    private Status status;

    private StatusFact statusFact;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public StatusFact getStatusFact() {
        return statusFact;
    }

    public void setStatusFact(StatusFact statusFact) {
        this.statusFact = statusFact;
    }
}
