package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.openapitools.jackson.nullable.JsonNullable;

@Data
public class TaskCreateDTO {
    @NotNull
    @Size(min = 1)
    private String title;

    private JsonNullable<Integer> index;

    private JsonNullable<String> content;

    @NotNull
    @JsonProperty("status")
    private String slug;

    @JsonProperty("assignee_id")
    private JsonNullable<Long> assigneeId;
}
