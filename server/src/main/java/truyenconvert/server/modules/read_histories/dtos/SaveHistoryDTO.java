package truyenconvert.server.modules.read_histories.dtos;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveHistoryDTO {
    private int bookId;
    private int chapter;
}
