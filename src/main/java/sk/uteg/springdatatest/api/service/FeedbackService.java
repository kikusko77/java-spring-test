package sk.uteg.springdatatest.api.service;

import org.springframework.stereotype.Service;
import sk.uteg.springdatatest.api.repository.FeedbackRepository;
import java.util.UUID;

@Service
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public long countFeedbackByCampaignId(UUID campaignId) {return feedbackRepository.countByCampaignId(campaignId);}
}
