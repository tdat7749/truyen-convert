package truyenconvert.server.modules.classifies.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateWorldContextDTO {
    private String title;
    private String description;
}
