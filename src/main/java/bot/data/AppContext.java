package bot.data;

import bot.service.telegram.ResponseHandler;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
public class AppContext {
    private ResponseHandler responseHandler;
}
