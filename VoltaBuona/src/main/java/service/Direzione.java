package service;

public enum Direzione {
    

    NORD,
    SUD,
    EST,
    OVEST;

      public static Direzione fromString(String s) {
        if (s == null) return null;
        String t = s.trim().toUpperCase();
        return switch (t) {
            case "N", "NORD" -> NORD;
            case "S", "SUD" -> SUD;
            case "E", "EST" -> EST;
            case "O", "OVEST" -> OVEST;
            default -> null;
        };
    }
}