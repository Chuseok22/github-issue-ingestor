package xyz.romrom.githubissueingestor.github.infrastructure.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.romrom.githubissueingestor.github.core.constant.SourceType;
import xyz.romrom.githubissueingestor.github.infrastructure.entity.IngestionCursor;
import xyz.romrom.githubissueingestor.github.infrastructure.repository.IngestionCursorRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class CursorService {

  private final IngestionCursorRepository ingestionCursorRepository;

  @Transactional(readOnly = true)
  public Optional<String> getCursor(String repositoryFullName, SourceType sourceType) {
    log.info("DB에서 레포지토리: {}, sourceType: {}에 대한 커서를 가져옵니다", repositoryFullName, sourceType);
    return ingestionCursorRepository.findByRepositoryFullNameAndSourceType(repositoryFullName, sourceType)
      .map(IngestionCursor::getCursorValue);
  }

  @Transactional
  public void upsertCursor(String repositoryFullName, SourceType sourceType, String cursorValue) {
    log.info("레포지토리: {}, sourceType: {}에 해당하는 커서 업데이트: cursorValue={}", repositoryFullName, sourceType, cursorValue);
    ingestionCursorRepository.findByRepositoryFullNameAndSourceType(repositoryFullName, sourceType)
      .ifPresentOrElse(
        ingestionCursor -> ingestionCursor.updateCursorValue(cursorValue),
        () -> ingestionCursorRepository.save(IngestionCursor.create(repositoryFullName, sourceType, cursorValue))
      );
  }
}
