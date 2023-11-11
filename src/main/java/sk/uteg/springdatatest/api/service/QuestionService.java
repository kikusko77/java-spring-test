package sk.uteg.springdatatest.api.service;

import org.springframework.stereotype.Service;
import sk.uteg.springdatatest.api.model.QuestionSummary;
import sk.uteg.springdatatest.api.repository.QuestionRepository;
import sk.uteg.springdatatest.db.model.Question;
import sk.uteg.springdatatest.db.model.QuestionType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final AnswerService answerService;

    public QuestionService(QuestionRepository questionRepository, AnswerService answerService) {
        this.questionRepository = questionRepository;
        this.answerService = answerService;
    }

    public List<QuestionSummary> getQuestionSummaries(UUID campaignId) {
        List<Question> questions = questionRepository.findByCampaignId(campaignId);
        List<QuestionSummary> questionSummaries = new ArrayList<>();

        for (Question question : questions) {
            QuestionSummary questionSummary = new QuestionSummary();
            questionSummary.setName(question.getText());
            questionSummary.setType(question.getType());

            if (question.getType() == QuestionType.RATING) {
                questionSummary.setRatingAverage(answerService.calculateAverageRating(question.getId()));
                questionSummary.setOptionSummaries(Collections.emptyList());
            } else {
                questionSummary.setOptionSummaries(answerService.countOptionOccurrences(question.getId()));
                questionSummary.setRatingAverage(BigDecimal.ZERO);
            }
            questionSummaries.add(questionSummary);
        }

        return questionSummaries;
    }
}
