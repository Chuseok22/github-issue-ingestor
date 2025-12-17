package xyz.romrom.githubissueingestor.github.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.romrom.githubissueingestor.github.core.constant.SourceType;

@Entity
@Table(
  name = "ingestion_cursor",
  uniqueConstraints =
  @UniqueConstraint(
    name = "uq_cursor", columnNames = {"repository_full_name", "source_type"}
  )
)
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IngestionCursor {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String repositoryFullName;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private SourceType sourceType;

  @Column(nullable = false)
  private String cursorValue;

  @Column(nullable = false)
  private Instant updatedAt;

  public static IngestionCursor create(String repositoryFullName, SourceType sourceType, String cursorValue) {
    return IngestionCursor.builder()
      .repositoryFullName(repositoryFullName)
      .sourceType(sourceType)
      .cursorValue(cursorValue)
      .updatedAt(Instant.now())
      .build();
  }

  public void updateCursorValue(String cursorValue) {
    this.cursorValue = cursorValue;
    this.updatedAt = Instant.now();
  }
}
