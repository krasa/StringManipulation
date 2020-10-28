package osmedile.intellij.stringmanip.utils;

import java.time.*;
import java.time.format.DateTimeParseException;

public class TimestampUtils {
    public static String decodeTimestamp(String selectedText) {
        try {
            long millis = Long.parseLong(selectedText);
            return Instant.ofEpochMilli(millis).toString();
        } catch (NumberFormatException | DateTimeException e) {
            return selectedText;
        }
    }


    public static String encodeTimestamp(String selectedText) {
        Instant instant = parseAny(selectedText);
        if (instant == null) return selectedText;
        return String.valueOf(instant.toEpochMilli());
    }

    private static Instant parseAny(String input) {
        for (int state = 0; ; ++state) {
            try {
                switch (state) {
                    case 0:
                        return Instant.parse(input);
                    case 1:
                        return ZonedDateTime.parse(input).toInstant();
                    case 2:
                        return OffsetDateTime.parse(input).toInstant();
                    case 3:
                        return LocalDateTime.parse(input).toInstant(ZoneOffset.UTC);
                    case 4:
                        return LocalDate.parse(input).atStartOfDay().toInstant(ZoneOffset.UTC);
                    default:
                        return null;
                }
            } catch (DateTimeParseException ignored) {
            }
        }
    }
}
