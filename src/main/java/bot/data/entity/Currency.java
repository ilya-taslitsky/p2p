package bot.data.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "currencies")
@Entity
@Data
@NoArgsConstructor
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String basicName;
    private String exchangeName;

    @ManyToMany(mappedBy = "currencies")
    private List<PaymentMethod> paymentMethods = new ArrayList<>();
}
