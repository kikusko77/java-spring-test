package sk.uteg.springdatatest.db.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Entity
@Data
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    private Question question;

    private String text;

    @Override
    public String toString() {
        return "Option{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }
}
