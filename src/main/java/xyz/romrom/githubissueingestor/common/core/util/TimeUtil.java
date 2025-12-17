package xyz.romrom.githubissueingestor.common.core.util;

import static xyz.romrom.githubissueingestor.common.core.util.CommonUtil.nvl;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    if (nvl(iso, "").isEmpty()) {
      throw new IllegalArgumentException("ISO 문자열이 null이거나 비어있습니다");
    }
    try {
      return Instant.parse(iso);
    } catch (DateTimeException e) {
      throw new IllegalArgumentException("ISO 문자열 파싱 실패: " + iso, e);
    }
  }
}
