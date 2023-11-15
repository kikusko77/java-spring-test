package sk.uteg.springdatatest.api.service;

import org.springframework.stereotype.Service;
import sk.uteg.springdatatest.api.model.OptionSummary;
import sk.uteg.springdatatest.api.repository.AnswerRepository;
import sk.uteg.springdatatest.api.repository.OptionRepository;
import java.math.BigDecimal;
import java.util.*;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;

    private final OptionRepository optionRepository;

    public AnswerService(AnswerRepository answerRepository,OptionRepository optionRepository) {
        this.answerRepository = answerRepository;
        this.optionRepository = optionRepository;
    }

    public BigDecimal calculateAverageRating(UUID questionId) {
        return answerRepository.calculateAverageRating(questionId);
    }

    public List<OptionSummary> countOptionOccurrences(UUID questionId) {
        List<Object[]> queryResults = optionRepository.countOptionOccurrences(questionId);
        List<OptionSummary> optionSummaries = new ArrayList<>();

        for (Object[] result : queryResults) {
            String optionText = (String) result[1];
            int occurrences = ((Number) result[2]).intValue();

            OptionSummary optionSummary = new OptionSummary();
            optionSummary.setText(optionText);
            optionSummary.setOccurrences(occurrences);
            optionSummaries.add(optionSummary);
        }

        return optionSummaries;
    }
}