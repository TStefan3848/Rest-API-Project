package stefan.toth.RestAPIProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseMessage {
    private final String status;
    private final String message;
}
