package truyenconvert.server.commons;

import lombok.*;
import org.springframework.http.HttpStatus;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseError {
    private HttpStatus httpStatus;
    private int httpStatusCode;
    private String message;
}
