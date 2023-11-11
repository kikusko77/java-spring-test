package sk.uteg.springdatatest.db.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    private Campaign campaign;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    private String text;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options;

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", type=" + type +
                ", campaignId=" + campaign.getId() +
                '}';
    }
}
