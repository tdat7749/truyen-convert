package truyenconvert.server.modules.classifies.dto;


import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditCategoryDTO {
    private String title;
    private String description;
}
