package sk.uteg.springdatatest.api.service;

import org.springframework.stereotype.Service;
import sk.uteg.springdatatest.api.model.CampaignSummary;
import java.util.UUID;

@Service
public class CampaignService {

    private final QuestionService questionService;

    private final FeedbackService feedbackService;

    public CampaignService(FeedbackService feedbackService,QuestionService questionService) {
        this.feedbackService = feedbackService;
        this.questionService = questionService;
    }

    public CampaignSummary getCampaignSummary(UUID campaignId) {
        CampaignSummary summary = new CampaignSummary();
        summary.setTotalFeedbacks(feedbackService.countFeedbackByCampaignId(campaignId));
        summary.setQuestionSummaries(questionService.getQuestionSummaries(campaignId));
        return summary;
    }
}
