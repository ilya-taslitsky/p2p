package bot.data.entity;

import javax.persistence.*;

import bot.data.ExchangeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
public class Client {
    @Id
    private String id;
    private Exchange exchange;
    private LocalDateTime timeToDelete;
    private String deleteDescription;
    @CreationTimestamp
    private LocalDateTime creationTime;

    public Client(String id, ExchangeEnum exchange) {
        this.id = id;
        this.exchange = exchange;
    }

    public Client(String id, ExchangeEnum exchange, LocalDateTime timeToDelete, String deleteDescription) {
        this.id = id;
        this.exchange = exchange;
        this.timeToDelete = timeToDelete;
        this.deleteDescription = deleteDescription;
    }
}

