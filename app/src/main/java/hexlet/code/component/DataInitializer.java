package hexlet.code.component;

import hexlet.code.dto.taskStatus.TaskStatusCreateDTO;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.CustomUserDetailsService;
import hexlet.code.service.TaskStatusService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private TaskStatusService taskStatusService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.findByEmail("aaa@bbb.com").isEmpty()) {
            var email = "aaa@bbb.com";
            var userData = new User();
            userData.setEmail(email);
            userData.setPassword("123");
            customUserDetailsService.createUser(userData);
        }

        var defaultStatuses = new ArrayList<TaskStatusCreateDTO>();
        defaultStatuses.add(new TaskStatusCreateDTO("Take", "take"));
        defaultStatuses.add(new TaskStatusCreateDTO("Completed", "completed"));

        var currentStatuses = taskStatusRepository.findAll().stream().map(TaskStatus::getSlug).toList();
        for (var status : defaultStatuses) {
            if (!currentStatuses.contains(status.getSlug())) {
                taskStatusService.create(status);
            }
        }
    }
}
