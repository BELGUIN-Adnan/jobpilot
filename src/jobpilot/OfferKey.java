package jobpilot;

public record OfferKey(String title, String company, String city) {

    public static OfferKey from(JobOffer offer) {
        return new OfferKey(offer.title().toLowerCase(), offer.company().toLowerCase(), offer.city().toLowerCase());
    }
}
