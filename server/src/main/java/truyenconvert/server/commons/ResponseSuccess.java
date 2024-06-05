package truyenconvert.server.commons;


import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseSuccess <T> {
    private String message;
    private T data;
}
