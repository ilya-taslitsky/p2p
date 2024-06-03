package bot.data;

import bot.data.entity.Client;
import lombok.Data;

import java.util.List;

@Data
public class ClientListDto {
    private List<Client> clients;
    private long amount;
    public static ClientListDto of(List<Client> clients) {
        ClientListDto clientListDto = new ClientListDto();
        clientListDto.setClients(clients);
        clientListDto.setAmount(clients.size());
        return clientListDto;
    }
}
