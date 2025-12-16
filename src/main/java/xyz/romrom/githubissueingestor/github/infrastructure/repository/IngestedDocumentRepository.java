package xyz.romrom.githubissueingestor.github.infrastructure.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import xyz.romrom.githubissueingestor.github.infrastructure.entity.IngestedDocument;

public interface IngestedDocumentRepository extends JpaRepository<IngestedDocument, UUID> {

}
