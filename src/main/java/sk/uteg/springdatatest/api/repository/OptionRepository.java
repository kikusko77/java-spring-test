package sk.uteg.springdatatest.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sk.uteg.springdatatest.db.model.Option;
import java.util.List;
import java.util.UUID;

public interface OptionRepository extends JpaRepository<Option, UUID> {

    @Query(value = "SELECT \"o\".\"id\", \"o\".\"text\", COUNT(\"aso\".\"option_id\") " +
            "FROM \"option\" \"o\" " +
            "LEFT JOIN \"answer_selected_option\" \"aso\" ON \"o\".\"id\" = \"aso\".\"option_id\" " +
            "WHERE \"o\".\"question_id\" = :questionId " +
            "GROUP BY \"o\".\"id\", \"o\".\"text\"", nativeQuery = true)
    List<Object[]> countOptionOccurrences(@Param("questionId") UUID questionId);
}
