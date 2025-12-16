package xyz.romrom.githubissueingestor.common.core.util;

import static xyz.romrom.githubissueingestor.common.core.util.CommonUtil.nvl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HashingUtil {

  public String sha256Hex(String value) {
    if (nvl(value, "").isEmpty()) {
      return sha256Hex("");
    }

    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] bytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));
      return toHex(bytes);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalArgumentException("SHA-256 알고리즘 사용 불가", e);
    }
  }

  public String toHex(byte[] bytes) {
    StringBuilder builder = new StringBuilder(bytes.length * 2);
    for (byte b : bytes) {
      builder.append(String.format("%02x", b));
    }
    return builder.toString();
  }
}
