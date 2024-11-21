package bot.data.dto;

import bot.data.ExchangeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@ToString
public class ClientDto {
    @NotEmpty(message = "Ох ты сука и ебало, ты id забыл долбаеб")
    private String id;
    @NotNull(message = "Фу сука как можно быть таким дауном и забыть exchange")
    private ExchangeEnum exchange;
    @Future(message = "Ты че долбаеб? Время должно быть в будущем")
    @NotNull(message = "Ты че долбаеб? Кто будет timeToDelete передавать?")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime timeToDelete;
    private String description;

}