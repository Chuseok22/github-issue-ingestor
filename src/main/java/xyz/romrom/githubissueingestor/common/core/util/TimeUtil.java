package xyz.romrom.githubissueingestor.common.core.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TimeUtil {

  private static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");

  /**
   * Instant -> LocalDateTime
   */
  public LocalDateTime instantToLocalDateTime(Instant instant) {
    if (instant == null) {
      return null;
    }
    return LocalDateTime.ofInstant(instant, ZONE_ID);
  }

  /**
   * LocalDateTime -> Instant
   */
  public Instant localDateTimeToInstant(LocalDateTime localDateTime) {
    if (localDateTime == null) {
      return null;
    }
    return localDateTime.atZone(ZONE_ID).toInstant();
  }

  public Instant isoToInstant(String iso) {
    try {
      return Instant.parse(iso);
    } catch (Exception e) {
      return Instant.now();
    }
  }
}
