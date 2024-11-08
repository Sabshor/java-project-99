package hexlet.code.service;

import hexlet.code.dto.TaskParamDTO;
import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.specification.TaskSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final LabelRepository labelRepository;
    private final TaskMapper taskMapper;
    private final TaskSpecification taskSpecification;

    public List<TaskDTO> getAll(TaskParamDTO params, PageRequest pageRequest) {
        var specification = taskSpecification.build(params);
        var tasks = taskRepository.findAll(specification, pageRequest);
        return tasks.stream().map(taskMapper::map).toList();
    }

    public TaskDTO show(Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        return taskMapper.map(task);
    }

    public TaskDTO create(TaskCreateDTO dto) {
        var task = taskMapper.map(dto);
        taskRepository.save(task);
        return taskMapper.map(task);

        /*var task = taskMapper.map(dto);
        User assignee = null;
        if (dto.getAssigneeId() != null) {
            assignee = userRepository.findById(dto.getAssigneeId().get()).orElse(null);
        }
        task.setAssignee(assignee);
        TaskStatus taskStatus = null;
        if (dto.getSlug() != null) {
            taskStatus = taskStatusRepository.findBySlug(dto.getSlug()).orElse(null);
        }
        task.setTaskStatus(taskStatus);
        List<Label> labels = null;
        if (dto.getTaskLabelIds() != null) {
            labels = labelRepository.findByIdIn(dto.getTaskLabelIds().get());
        }
        task.setLabels(labels);
        taskRepository.save(task);
        return taskMapper.map(task);*/
    }

    public TaskDTO update(TaskUpdateDTO dto, Long taskId) {
        var task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id: " + taskId + " not found."));
        taskMapper.update(dto, task);
        taskRepository.save(task);
        return taskMapper.map(task);


       /* taskMapper.update(dto, task);
        User assignee = null;
        if (dto.getAssigneeId() != null) {
            assignee = userRepository.findById(dto.getAssigneeId().get()).orElse(null);
        }
        task.setAssignee(assignee);
        if (dto.getSlug() != null) {
            TaskStatus taskStatus = taskStatusRepository.findBySlug(dto.getSlug().get()).orElse(null);
            task.setTaskStatus(taskStatus);
        }
        if (dto.getTaskLabelIds() != null) {
            List<Label> labels = null;
            labels = labelRepository.findByIdIn((dto.getTaskLabelIds()).get());
            task.setLabels(labels);
        }
        taskRepository.save(task);
        return taskMapper.map(task);*/
    }

    public void destroy(Long id) {
        taskRepository.deleteById(id);
    }
}
