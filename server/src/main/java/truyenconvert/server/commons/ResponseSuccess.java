package truyenconvert.server.commons;


import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseSuccess <T> {
    private String message;
    private T data;
}
