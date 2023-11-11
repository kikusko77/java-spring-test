package sk.uteg.springdatatest.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.uteg.springdatatest.db.model.Feedback;
import java.util.UUID;

public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {

    long countByCampaignId(UUID campaignId);
}
