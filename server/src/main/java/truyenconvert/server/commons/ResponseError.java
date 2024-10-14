package truyenconvert.server.commons;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseError implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private HttpStatus httpStatus;
    private int httpStatusCode;
    private String message;
}
