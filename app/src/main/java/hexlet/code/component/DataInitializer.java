package hexlet.code.component;

import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {
    //@Autowired
    //private final UserRepository userRepository;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private final UserMapper userMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        /*var userData = new UserCreateDTO();
        userData.setEmail("aaa@bbb.com");
        userData.setPassword("123");
        var user = userMapper.map(userData);
        userRepository.save(user);*/
        var userData = new User();
        userData.setEmail("aaa@bbb.com");
        userData.setPassword("123");
        customUserDetailsService.createUser(userData);
    }
}
