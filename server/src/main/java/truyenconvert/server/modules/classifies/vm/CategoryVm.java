package truyenconvert.server.modules.classifies.vm;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CategoryVm {
    private int id;
    private String title;
    private String description;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;
}
