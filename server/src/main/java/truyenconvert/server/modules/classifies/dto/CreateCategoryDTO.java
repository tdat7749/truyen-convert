package truyenconvert.server.modules.classifies.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCategoryDTO {
    private String title;
    private String description;
}
