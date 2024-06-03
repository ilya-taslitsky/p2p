package bot.data.entity;

import javax.persistence.*;

import bot.data.Exchange;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clients")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Id
    private String id;
    @Enumerated(value = EnumType.STRING)
    private Exchange exchange;
}

