package com.example.bybitp2p.data;

import com.example.bybitp2p.service.telegram.ResponseHandler;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class AppContext {
    private ResponseHandler responseHandler;
}
