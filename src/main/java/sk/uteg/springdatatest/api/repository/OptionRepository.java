package sk.uteg.springdatatest.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.uteg.springdatatest.db.model.Option;
import java.util.List;
import java.util.UUID;

public interface OptionRepository extends JpaRepository<Option, UUID> {
    List<Option> findByQuestionId(UUID questionId);
}
