package jobpilot;

import java.util.List;
import java.util.Map;
public class JobPilotTests {
    public static void main(String[] args) {
        runTests();
    }

    public static void runTests() {
        JobOffer ntt = new JobOffer("  Backend Developer ", "NTT DATA", "Tetouan", 9000);
        Application app = new Application(ntt);
        Application other = new Application(ntt);
        ApplicationTracker tracker = new ApplicationTracker();
        Application a1 = tracker.add(new JobOffer("Backend Developer", "NTT DATA", "Tetouan", 9000));
        Application a2 = tracker.add(new JobOffer("Java Developer", "Capgemini", "Casablanca", 11000));
        Application a3 = tracker.add(new JobOffer("Junior Developer", "StartupX", "Remote", 0));


        //ApplicationTracker JUNIT like tests
        check("JP-4 add returns the application", a1 != null && a1.getOffer().company().equals("NTT DATA")
                && a3 != null && tracker.size() == 3);
        check("JP-4 null offer refused", tracker.add(null) == null);
        check("JP-4 findById", tracker.findById(a2.getId()) == a2 && tracker.findById(-1) == null);
        check("JP-4 changeStatus", tracker.changeStatus(a1.getId(), Status.INTERVIEW) && a1.getStatus() == Status.INTERVIEW);
        check("JP-4 changeStatus refused", !tracker.changeStatus(a1.getId(), Status.APPLIED)
                && !tracker.changeStatus(-1, Status.REJECTED));
        List<Application> copy = tracker.all();
        copy.set(0, null);
        check("JP-4 all() is a copy", copy.size() == 3 && tracker.all().getFirst() == a1);
        tracker.changeStatus(a2.getId(), Status.REJECTED);
        check("JP-4 byStatus", tracker.byStatus(Status.INTERVIEW).size() == 1
                && tracker.byStatus(Status.INTERVIEW).getFirst() == a1 && tracker.byStatus(Status.OFFER).isEmpty());

        check("JP-4 count", tracker.count(Status.APPLIED) == 1 && tracker.count(Status.REJECTED) == 1);

        //ApplicationTracker JP-5: Statistics JUNIT like tests
        check("JP-5 response rate", Math.abs(tracker.responseRate() - 66.666) < 0.01);
        check("JP-5 average known salary", tracker.averageSalary() == 10000.0);
        check("JP-5 no offer yet", tracker.bestOffer() == null);
        tracker.changeStatus(a1.getId(), Status.OFFER);
        check("JP-5 best offer", tracker.bestOffer() == a1);
        ApplicationTracker empty = new ApplicationTracker();
        check("JP-5 empty tracker", empty.responseRate() == 0.0 && empty.averageSalary() == 0.0
                && empty.bestOffer() == null);

        check("JP-6 mixed case fromText", Status.fromText("InTeRvIeW") == Status.INTERVIEW);
        check("JP-6 remote mixed case", new JobOffer("Dev", "X", "ReMoTe", 0).isRemote());

        //Application JUNIT like tests
        check("JP-3 starts APPLIED", app.getStatus() == Status.APPLIED);
        check("JP-3 ids go up by 1", other.getId() == app.getId() + 1);
        check("JP-3 allowed move", app.moveTo(Status.INTERVIEW) && app.getStatus() == Status.INTERVIEW);
        check("JP-3 refused move", !app.moveTo(Status.APPLIED) && app.getStatus() == Status.INTERVIEW);
        check("JP-3 toString", app.toString().equals("#" + app.getId() + " Backend Developer @ NTT DATA (Tetouan) [INTERVIEW]"));

        //jobOffer JUNIT like tests
        check("JP-2 cleaned", ntt.title().equals("Backend Developer"));
        check("JP-2 label", ntt.label().equals("Backend Developer @ NTT DATA (Tetouan)"));
        check("JP-2 unknown salary", new JobOffer("Dev", "X", "Rabat", -5).salary() == 0
                && !new JobOffer("Dev", "X", "Rabat", 0).hasSalary() && ntt.hasSalary());
        check("JP-2 remote", new JobOffer("Dev", "X", " REMOTE ", 0).isRemote() && !ntt.isRemote());
        check("JP-2 equals", ntt.equals(new JobOffer("Backend Developer", "NTT DATA", "Tetouan", 9000)));

        //Status JUNIT like tests
        check("JP-1 applied → interview", Status.APPLIED.canMoveTo(Status.INTERVIEW));
        check("JP-1 applied → rejected", Status.APPLIED.canMoveTo(Status.REJECTED));
        check("JP-1 no skipping a step", !Status.APPLIED.canMoveTo(Status.OFFER));
        check("JP-1 interview → offer", Status.INTERVIEW.canMoveTo(Status.OFFER));
        check("JP-1 no going back", !Status.INTERVIEW.canMoveTo(Status.APPLIED));
        check("JP-1 final statuses", Status.OFFER.isFinal() && Status.REJECTED.isFinal()
                && !Status.APPLIED.isFinal() && !Status.INTERVIEW.isFinal());
        check("JP-1 nothing after a final status", !Status.REJECTED.canMoveTo(Status.INTERVIEW)
                && !Status.OFFER.canMoveTo(Status.REJECTED));
        check("JP-1 null refused", !Status.APPLIED.canMoveTo(null));
        check("JP-1 fromText", Status.fromText(" interview ") == Status.INTERVIEW && Status.fromText("OFFER") == Status.OFFER);
        check("JP-1 fromText unknown", Status.fromText("banana") == null && Status.fromText(null) == null);


        ApplicationTracker track = new ApplicationTracker();

        JobOffer offer1 = new JobOffer("Java Developer", "Acme", "Casablanca", 12000);
        JobOffer offer2 = new JobOffer("Java Developer", "Acme", "Casablanca", 12000);

        Application first = track.add(offer1);
        Application second = track.add(offer2);

        check("JP-4 duplicate offer returns null", second == null);
        check("JP-4 duplicate offer does not change size", track.size() == 1);
        check("JP-1 fromText with extra words", Status.fromText("fdgjdlkj offer") == null);

        //PO new rule checker
        ApplicationTracker cities = new ApplicationTracker();
        cities.add(new JobOffer("Java Developer", "NTT DATA", "Tetouan", 9000));
        check("JP-4 same job, other city is not a duplicate",
                cities.add(new JobOffer("java developer", "ntt data", "Casablanca", 9000)) != null);
        check("JP-4 duplicate ignores case and salary",
                cities.add(new JobOffer("JAVA DEVELOPER", "Ntt Data", "tetouan", 12000)) == null);

        ApplicationTracker big = new ApplicationTracker();
        for (int i = 1; i <= 100; i++) {
            big.add(new JobOffer("Developer " + i, "Company", "Tetouan", 0));
        }
        check("JP-7 no capacity limit", big.size() == 100);

        //ApplicationTracker JP-8: Statistics in one pass
        ApplicationTracker stats = new ApplicationTracker();
        Application s1 = stats.add(new JobOffer("Dev A", "X", "Tetouan", 0));
        stats.add(new JobOffer("Dev B", "Y", "Rabat", 0));
        stats.changeStatus(s1.getId(), Status.INTERVIEW);
        Map<Status, Integer> counts = stats.countByStatus();
        check("JP-8 countByStatus", counts.get(Status.APPLIED) == 1 && counts.get(Status.INTERVIEW) == 1);
        check("JP-8 every status is there, even with 0", counts.size() == 4 && counts.get(Status.OFFER) == 0);
        check("JP-8 empty tracker", new ApplicationTracker().countByStatus().get(Status.REJECTED) == 0);




    }

    private static void check(String testName, boolean passed) {
        System.out.println((passed ? "PASS: " : "FAIL: ") + testName);
    }
}
