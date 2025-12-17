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
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.romrom.githubissueingestor.github.core.constant.SourceType;

@Entity
@Table(
  name = "ingested_document",
  uniqueConstraints =
  @UniqueConstraint(
    name = "uq_doc",
    columnNames = {"repository_full_name", "source_type", "source_key"}
  )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IngestedDocument {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String repositoryFullName;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private SourceType sourceType;

  @Column(nullable = false)
  private String sourceKey;

  @Column(nullable = false)
  private UUID qdrantPointId;

  @Column(nullable = false)
  private String contentHash;

  private Instant sourceUpdatedAt;

  @Column(nullable = false)
  private Instant ingestedAt;

  public IngestedDocument(String repositoryFullName, SourceType sourceType, String sourceKey, UUID qdrantPointId, String contentHash, Instant sourceUpdatedAt, Instant ingestedAt) {
    this.repositoryFullName = repositoryFullName;
    this.sourceType = sourceType;
    this.sourceKey = sourceKey;
    this.qdrantPointId = qdrantPointId;
    this.contentHash = contentHash;
    this.sourceUpdatedAt = sourceUpdatedAt;
    this.ingestedAt = ingestedAt;
  }
}
