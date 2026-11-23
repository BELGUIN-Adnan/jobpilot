package jobpilot;

import java.util.Arrays;

public class ApplicationTracker {

    private final Application[] applications;
    private int count = 0;

    public ApplicationTracker(int capacity) {
        applications = new Application[capacity];
    }

    public Application add(JobOffer offer) {
        if (offer == null || count == applications.length) {
            return null;
        }
        for (int i = 0; i < count; i++) {
            JobOffer existing = applications[i].getOffer();
            if (existing.title().equalsIgnoreCase(offer.title())
                    && existing.company().equalsIgnoreCase(offer.company())
                    && existing.city().equalsIgnoreCase(offer.city())) {
                return null;
            }
        }
        Application newApp = new Application(offer);
        applications[count] = newApp;
        count++;
        return newApp;
    }

    public int size() {
        return count;
    }

    public Application findById(int id) {
        if (id <= 0) {
            return null;
        }
        for (int i = 0; i < count; i++) {
            if (applications[i].getId() == id) {
                return applications[i];
            }
        }
        return null;
    }

    public boolean changeStatus(int id, Status next) {
        if (next == null) {
            return false;
        }
        Application foundApp = findById(id);
        if (foundApp != null) {
            return foundApp.moveTo(next);
        }
        return false;
    }

    public Application[] all() {
        return Arrays.copyOf(applications, count);
    }

    public int count(Status status) {
        if (status == null) {
            return 0;
        }
        int countStatus = 0;
        for (int i = 0; i < count; i++) {
            if (applications[i].getStatus() == status) {
                countStatus++;
            }
        }
        return countStatus;
    }

    public Application[] byStatus(Status status) {
        if (status == null) {
            return new Application[0];
        }
        Application[] applicationsByStatus = new Application[count(status)];
        int index = 0;
        for (int i = 0; i < count; i++) {
            if (applications[i].getStatus() == status) {
                applicationsByStatus[index++] = applications[i];
            }
        }
        return applicationsByStatus;
    }

    public double responseRate() {
        if (this.count == 0) {
            return 0.0;
        }
        return (this.count - count(Status.APPLIED)) * 100.0 / this.count;
    }

    public double averageSalary() {
        int averageSalary = 0;
        int count = 0;
        for (int i = 0; i < this.count; i++) {
            if (applications[i].getOffer().hasSalary()) {
                count++;
                averageSalary += applications[i].getOffer().salary();
            }
        }
        if (count == 0) {
            return 0.0;
        }
        return (double) averageSalary / count;
    }

    public Application bestOffer() {
        Application[] offerApplication = byStatus(Status.OFFER);
        if (offerApplication.length == 0) {
            return null;
        }
        Application best = null;
        for (int i = 0; i < offerApplication.length; i++) {
            if (best == null || offerApplication[i].getOffer().salary() > best.getOffer().salary()) {
                best = offerApplication[i];
            }
        }
        return best;
    }
}
