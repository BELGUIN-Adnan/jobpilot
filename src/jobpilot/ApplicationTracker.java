package jobpilot;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class ApplicationTracker {

    private final List<Application> applications = new ArrayList<>();

    public Application add(JobOffer offer) {
        if (offer == null) {
            return null;
        }
        for (Application application : applications) {
            JobOffer existing = application.getOffer();
            if (existing.title().equalsIgnoreCase(offer.title())
                    && existing.company().equalsIgnoreCase(offer.company())
                    && existing.city().equalsIgnoreCase(offer.city())) {
                return null;
            }
        }
        Application newApp = new Application(offer);
        applications.add(newApp);
        return newApp;
    }

    public int size() {
        return applications.size();
    }

    public Application findById(int id) {
        if (id <= 0) {
            return null;
        }
        for (Application application : applications) {
            if (application.getId() == id) {
                return application;
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

    public List<Application> all() {
        return new ArrayList<>(applications);
    }

    public int count(Status status) {
        if (status == null) {
            return 0;
        }
        int countStatus = 0;
        for (Application application : applications) {
            if (application.getStatus() == status) {
                countStatus++;
            }
        }
        return countStatus;
    }

    public List<Application> byStatus(Status status) {
        List<Application> result = new ArrayList<>();
        for (Application application : applications) {
            if (application.getStatus() == status) {
                result.add(application);
            }
        }
        return result;
    }

    public double responseRate() {
        if (this.size() == 0) {
            return 0.0;
        }
        return (this.size() - count(Status.APPLIED)) * 100.0 / this.size();
    }

    public double averageSalary() {
        int averageSalary = 0;
        int count = 0;
        for (Application application : applications) {
            if (application.getOffer().hasSalary()) {
                count++;
                averageSalary += application.getOffer().salary();
            }
        }
        if (count == 0) {
            return 0.0;
        }
        return (double) averageSalary / count;
    }

    public Application bestOffer() {
        List<Application> offerApplication = byStatus(Status.OFFER);
        if (offerApplication.isEmpty()) {
            return null;
        }
        Application best = null;
        for (Application application : offerApplication) {
            if (best == null || application.getOffer().salary() > best.getOffer().salary()) {
                best = application;
            }
        }
        return best;
    }

    public Map<Status, Integer> countByStatus() {
        Map<Status, Integer> result = new HashMap<>();
        for (Status status : Status.values()) {
            result.put(status, 0);
        }
        for (Application application : applications) {
            Status status = application.getStatus();
            result.put(status, result.get(status) + 1);
        }
        return result;
    }

}
