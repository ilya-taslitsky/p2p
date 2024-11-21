package bot.data.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "payment_methods")
@Data
@NoArgsConstructor
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String basicName;
    private String exchangeName;
    @ManyToOne
    @JoinColumn(name = "exchange_id")
    private Exchange exchange;
    @ManyToMany
    @JoinTable(
            name = "payment_methods_currencies",
            joinColumns = @JoinColumn(name = "payment_method_id"),
            inverseJoinColumns = @JoinColumn(name = "currency_id")
    )
    private List<Currency> currencies = new ArrayList<>();
}
