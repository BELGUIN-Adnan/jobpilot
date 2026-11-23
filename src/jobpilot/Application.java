package jobpilot;

public class Application {
    private static int automaticId = 1;
    private final int id;
    private Status appStatus;
    private final JobOffer offer;

    public Application(JobOffer offer) {
        this.offer = offer;
        this.appStatus = Status.APPLIED;
        id = automaticId;
        automaticId++;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return appStatus;
    }

    public JobOffer getOffer() {
        return offer;
    }

    public boolean moveTo(Status nextStatus) {
        if (appStatus.canMoveTo(nextStatus)) {
            appStatus = nextStatus;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("#%d %s [%s]", id, offer.label(), appStatus);
    }
}
