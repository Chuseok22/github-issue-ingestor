package xyz.romrom.githubissueingestor.github.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import xyz.romrom.githubissueingestor.github.core.constant.SourceType;
import xyz.romrom.githubissueingestor.github.infrastructure.entity.IngestionCursor;

public interface IngestionCursorRepository extends JpaRepository<IngestionCursor, UUID> {

  Optional<IngestionCursor> findByRepositoryFullNameAndSourceType(String repositoryFullName, SourceType sourceType);

}
