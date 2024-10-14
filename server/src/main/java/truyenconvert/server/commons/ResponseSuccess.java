package truyenconvert.server.commons;


import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseSuccess <T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String message;
    private T data;
}
