package hexlet.code.component;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.taskstatus.TaskStatusCreateDTO;
import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.CustomUserDetailsService;
import hexlet.code.service.LabelService;
import hexlet.code.service.TaskStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private final UserRepository userRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final TaskStatusService taskStatusService;
    private final LabelRepository labelRepository;
    private final LabelService labelService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.findByEmail("aaa@bbb.com").isEmpty()) {
            var email = "aaa@bbb.com";
            var userData = new User();
            userData.setEmail(email);
            userData.setPassword("123");
            customUserDetailsService.createUser(userData);
        }
        if (userRepository.findByEmail("hexlet@example.com").isEmpty()) {
            // need for hexlet autotests
            var email = "hexlet@example.com";
            var userData = new User();
            userData.setEmail(email);
            userData.setPassword("qwerty");
            customUserDetailsService.createUser(userData);
        }

        var defaultStatuses = new ArrayList<TaskStatusCreateDTO>();
        // need for hexlet autotests
        defaultStatuses.add(new TaskStatusCreateDTO("Draft", "draft"));
        defaultStatuses.add(new TaskStatusCreateDTO("toReview", "to_review"));
        defaultStatuses.add(new TaskStatusCreateDTO("toBeFixed", "to_be_fixed"));
        defaultStatuses.add(new TaskStatusCreateDTO("toPublish", "to_publish"));
        defaultStatuses.add(new TaskStatusCreateDTO("Published", "published"));
        // for my test
        //defaultStatuses.add(new TaskStatusCreateDTO("Take", "take"));
        //defaultStatuses.add(new TaskStatusCreateDTO("Completed", "completed"));
        var currentStatuses = taskStatusRepository.findAll().stream().map(TaskStatus::getSlug).toList();
        for (var status : defaultStatuses) {
            if (!currentStatuses.contains(status.getSlug())) {
                taskStatusService.create(status);
            }
        }

        var labelFeature = new LabelCreateDTO();
        labelFeature.setName("feature");
        var labelBug = new LabelCreateDTO();
        labelBug.setName("bug");
        var defaultLabels = new ArrayList<LabelCreateDTO>();
        defaultLabels.add(labelFeature);
        defaultLabels.add(labelBug);
        var currentLabels = labelRepository.findAll().stream().map(Label::getName).toList();
        for (var label : defaultLabels) {
            if (!currentLabels.contains(label.getName())) {
                labelService.create(label);
            }
        }
    }
}
