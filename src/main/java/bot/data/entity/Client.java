package bot.data.entity;

import javax.persistence.*;

import bot.data.Exchange;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
public class Client {
    @Id
    private String id;
    @Enumerated(value = EnumType.STRING)
    private Exchange exchange;
    private LocalDateTime timeToDelete;
    private String deleteDescription;
    @CreationTimestamp
    private LocalDateTime creationTime;

    public Client(String id, Exchange exchange) {
        this.id = id;
        this.exchange = exchange;
    }

    public Client(String id, Exchange exchange, LocalDateTime timeToDelete, String deleteDescription) {
        this.id = id;
        this.exchange = exchange;
        this.timeToDelete = timeToDelete;
        this.deleteDescription = deleteDescription;
    }
}

