package xyz.romrom.githubissueingestor.common.core.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonUtil {

  /**
   * null 문자 처리 -> str1이 null 인 경우 str2 반환
   * "null" 문자열 처리 -> str1이 "null" 인 경우 str2 반환
   * str1이 빈 문자열 or 공백인 경우 -> str2 반환
   *
   * @param str1 검증할 문자열
   * @param str2 str1 이 null 인경우 반환할 문자열
   * @return null 이 아닌 문자열
   */
  public static String nvl(String str1, String str2) {
    if (str1 == null) { // str1 이 null 인 경우
      return str2;
    } else if (str1.equals("null")) { // str1 이 문자열 "null" 인 경우
      return str2;
    } else if (str1.isBlank()) { // str1 이 "" or " " 인 경우
      return str2;
    }
    return str1;
  }
}
