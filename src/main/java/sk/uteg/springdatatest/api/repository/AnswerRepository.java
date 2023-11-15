package sk.uteg.springdatatest.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sk.uteg.springdatatest.db.model.Answer;
import java.math.BigDecimal;
import java.util.UUID;

public interface AnswerRepository extends JpaRepository<Answer, UUID> {

    @Query("SELECT AVG(a.ratingValue) FROM Answer a WHERE a.question.id = :questionId AND a.ratingValue > 0")
    BigDecimal calculateAverageRating(@Param("questionId") UUID questionId);
}
