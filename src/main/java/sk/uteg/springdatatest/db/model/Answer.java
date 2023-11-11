package sk.uteg.springdatatest.db.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(optional = false)
    private Feedback feedback;
    @ManyToOne(optional = false)
    private Question question;
    private int ratingValue;

    @ManyToMany
    @JoinTable(name = "answer_selected_option",
            joinColumns = @JoinColumn(name = "answer_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "option_id", referencedColumnName = "id")
    )
    private List<Option> selectedOptions;

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", ratingValue=" + ratingValue +
                ", feedbackId=" + feedback.getId() +
                ", questionId=" + question.getId() +
                '}';
    }
}
