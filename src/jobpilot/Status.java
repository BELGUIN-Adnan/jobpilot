package jobpilot;

public enum Status {
    APPLIED,
    INTERVIEW,
    OFFER,
    REJECTED;

    public boolean canMoveTo(Status text) {
        if (text == null || this == text) {
            return false;
        }
        return switch (this) {
            case APPLIED -> text == INTERVIEW || text == REJECTED;
            case INTERVIEW -> text == OFFER || text == REJECTED;
            case OFFER, REJECTED -> false;
        };
    }

    public boolean isFinal() {
        return this == OFFER || this == REJECTED;
    }

    public static Status fromText(String text) {
        if (text == null) {
            return null;
        }
        for (Status s : Status.values()) {
            if (s.name().equalsIgnoreCase(text.trim())) {
                return s;
            }
        }
        return null;
    }
}
