package hexlet.code.controller;

import hexlet.code.dto.taskstatus.TaskStatusCreateDTO;
import hexlet.code.dto.taskstatus.TaskStatusDTO;
import hexlet.code.dto.taskstatus.TaskStatusUpdateDTO;
import hexlet.code.service.TaskStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/task_statuses")
public class TaskStatusController {
    private final TaskStatusService taskStatusService;

    @GetMapping(path = "/{id}")
    public TaskStatusDTO getById(@PathVariable Long id) {
        return taskStatusService.show(id);
    }

    @GetMapping()
    public ResponseEntity<List<TaskStatusDTO>> getAll() {
        var statuses = taskStatusService.getAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(statuses.size()))
                .body(statuses);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatusDTO create(@Valid @RequestBody TaskStatusCreateDTO data) {
        return taskStatusService.create(data);
    }

    @PutMapping(path = "/{id}")
    public TaskStatusDTO update(@Valid @RequestBody TaskStatusUpdateDTO data, @PathVariable Long id) {
        return taskStatusService.update(data, id);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskStatusService.destroy(id);
    }
}
