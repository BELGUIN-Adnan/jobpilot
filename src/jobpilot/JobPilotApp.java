package jobpilot;

import java.util.Scanner;
import java.util.List;

public class JobPilotApp {

    public static void main(String[] args) {
        int choice;
        Scanner scanner = new Scanner(System.in);
        ApplicationTracker tracker = new ApplicationTracker();
        do {
            menu();
            System.out.print("Choose: ");
            choice = choice(scanner);
            switch (choice) {
                case 1 -> {
                    Application app = tracker.add(addJobOffer(scanner));
                    if (app == null) {
                        System.out.println("Not added: this offer is already in your list.");
                    } else {
                        System.out.printf("Saved as #%d%n", app.getId());
                    }
                }
                case 2 -> listAllApps(tracker);
                case 3 -> changeStatus(tracker, scanner);
                case 4 -> {
                    System.out.print("enter the status: ");
                    listAppsByStatus(tracker, scanner.nextLine());
                }
                case 5 -> statistics(tracker);
                case 0 -> System.out.println("Goodbye!");
                default -> System.out.println("Unknown choice. ");
            }
        } while (choice != 0);
    }

    public static void menu() {
        // this is from Udemy course. I remembered it the """
        String menu = """
                === JobPilot v1 ===
                1. Add an application
                2. List all applications
                3. Change a status
                4. Filter by status
                5. Statistics
                0. Quit
                """;
        System.out.print(menu);
    }

    public static JobOffer addJobOffer(Scanner input) {
        String title;
        String company;
        String city;
        int salary;
        System.out.print("enter the offer title : ");
        title = input.nextLine();
        System.out.print("enter the offer company : ");
        company = input.nextLine();
        System.out.print("enter the offer city : ");
        city = input.nextLine();
        System.out.print("enter the offer salary : ");
        salary = choice(input);
        return new JobOffer(title, company, city, salary);
    }

    public static int choice(Scanner scanner) {
        while (true) {
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine();
                return choice;
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
    }

    public static void listAppsByStatus(ApplicationTracker tracker, String text) {
        Status status = Status.fromText(text);
        if (status == null) {
            System.out.println("Unknown status.");
            return;
        }
        List<Application> appByStatus = tracker.byStatus(status);
        if (appByStatus.isEmpty()) {
            System.out.println("No applications with status " + status + ".");
        } else {
            for (Application app : appByStatus) {
                System.out.println(app);
            }
        }
    }

    public static void listAllApps(ApplicationTracker tracker) {

        List<Application> apps = tracker.all();
        if (tracker.size() == 0) {
            System.out.println("No applications yet.");
        } else {
            for (Application app : apps) {
                System.out.println(app);
            }
        }
    }

    public static void changeStatus(ApplicationTracker tracker, Scanner scanner) {
        System.out.print("enter the id : ");
        int choice = choice(scanner);
        if (tracker.findById(choice) == null) {
            System.out.println("unknown id");
        } else {
            System.out.print("enter the status : ");
            String status = scanner.nextLine();
            Status nextStatus = Status.fromText(status);
            if (nextStatus == null) {
                System.out.println("unknown status.");
            } else if (tracker.changeStatus(choice, nextStatus)) {
                System.out.println("done.");
            } else System.out.printf("Not allowed: %s → %s%n",
                    tracker.findById(choice).getStatus(), nextStatus);
        }
    }

    public static void statistics(ApplicationTracker tracker) {
        for (Status status : Status.values()) {
            System.out.printf("%s: %d%n", status, tracker.count(status));
        }
        System.out.printf("Response rate: %.1f%%%n", tracker.responseRate());
        System.out.printf("Average known salary: %.2f MAD%n", tracker.averageSalary());
        if (tracker.bestOffer() == null) {
            System.out.println("Best offer: none yet");
        } else {
            System.out.printf("Best offer: %s%n", tracker.bestOffer().getOffer().label());
        }
    }
}
