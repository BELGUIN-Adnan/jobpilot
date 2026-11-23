package jobpilot;

public record JobOffer(String title, String company, String city, int salary) {
    public JobOffer {
        title = title == null ? "" : title.trim();
        company = company == null ? "" : company.trim();
        city = city == null ? "" : city.trim();
        salary = salary < 0 ? 0 : salary;
    }

    public boolean hasSalary() {
        return salary > 0;
    }

    public boolean isRemote() {
        return city.equalsIgnoreCase("remote");
    }

    public String label() {
        return String.format("%s @ %s (%s)", title, company, city);
    }
}
