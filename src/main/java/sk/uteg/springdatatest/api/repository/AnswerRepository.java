package sk.uteg.springdatatest.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.uteg.springdatatest.db.model.Answer;
import java.util.List;
import java.util.UUID;

public interface AnswerRepository extends JpaRepository<Answer, UUID> {
    List<Answer> findByQuestionId(UUID questionId);
}
