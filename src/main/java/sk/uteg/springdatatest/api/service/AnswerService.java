package sk.uteg.springdatatest.api.service;

import org.springframework.stereotype.Service;
import sk.uteg.springdatatest.api.model.OptionSummary;
import sk.uteg.springdatatest.api.repository.AnswerRepository;
import sk.uteg.springdatatest.api.repository.OptionRepository;
import sk.uteg.springdatatest.db.model.Answer;
import sk.uteg.springdatatest.db.model.Option;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

        List<Answer> answers = answerRepository.findByQuestionId(questionId);

        BigDecimal sum = BigDecimal.ZERO;
        int count = 0;

        for (Answer answer : answers) {
            if (answer.getRatingValue() > 0) {
                sum = sum.add(BigDecimal.valueOf(answer.getRatingValue()));
                count++;
            }
        }

        return (count > 0) ? sum.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
    }

    public List<OptionSummary> countOptionOccurrences(UUID questionId) {
        List<Option> options = optionRepository.findByQuestionId(questionId);
        Map<UUID, OptionSummary> optionCountMap = new HashMap<>();

        for (Option option : options) {
            OptionSummary optionSummary = new OptionSummary();
            optionSummary.setText(option.getText());
            optionSummary.setOccurrences(0);
            optionCountMap.put(option.getId(), optionSummary);
        }

        List<Answer> answers = answerRepository.findByQuestionId(questionId);

        for (Answer answer : answers) {
            for (Option selectedOption : answer.getSelectedOptions()) {
                OptionSummary optionSummary = optionCountMap.get(selectedOption.getId());
                if (optionSummary != null) {
                    optionSummary.setOccurrences(optionSummary.getOccurrences() + 1);
                }
            }
        }

        return new ArrayList<>(optionCountMap.values());
    }
}