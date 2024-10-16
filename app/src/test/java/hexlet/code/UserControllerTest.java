package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.UserCreateDTO;
import hexlet.code.dto.UserUpdateDTO;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRepository userRepository;
    //@Autowired
    //private Faker faker;
    private User testUser;

    @BeforeEach
    public void setUser() {
        Faker faker = new Faker();

        testUser = Instancio.of(User.class)
                .ignore(Select.field(User::getUpdatedAt))
                .ignore(Select.field(User::getCreatedAt))
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(User::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(User::getLastName), () -> faker.name().lastName())
                .supply(Select.field(User::getPassword), () -> faker.internet().password())
                .create();
    }

    @Test
    public void testIndex() throws Exception {
        userRepository.save(testUser);

        var result = mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        userRepository.save(testUser);

        var request = get("/api/users/" + testUser.getId());

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("firstName").isEqualTo(testUser.getFirstName()),
                v -> v.node("lastName").isEqualTo(testUser.getLastName()),
                v -> v.node("email").isEqualTo(testUser.getEmail()));
    }

    @Test
    public void testCreate() throws Exception {
        var createdUser = new UserCreateDTO();
        createdUser.setPassword("123");
        createdUser.setEmail("aaaa@bbbb.com");

        var request = post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createdUser));

        mockMvc.perform(request).andExpect(status().isCreated());

        var user = userRepository.findByEmail(createdUser.getEmail()).get();

        assertNotNull(user);
        assertThat(user.getEmail()).isEqualTo(createdUser.getEmail());
    }

    @Test
    public void testUpdate() throws Exception {
        userRepository.save(testUser);
        var updatedData = new UserUpdateDTO();


        //updatedData.setEmail(JsonNullable.of("111@gmail.com"));
        updatedData.setFirstName(JsonNullable.of("UpdatedName"));

        var request = put("/api/users/" + testUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedData));

        mockMvc.perform(request).andExpect(status().isOk());

        var updatedUser = userRepository.findById(testUser.getId()).get();

        //assertThat(updatedUser.getEmail()).isEqualTo(updatedData.getEmail().get());
        assertThat(updatedUser.getFirstName()).isEqualTo(updatedData.getFirstName().get());



       /* testUser.setPassword("ewrew");
        userRepository.save(testUser);

        var updateDto = new UserUpdateDTO();
        updateDto.setEmail(JsonNullable.of(testUser.getEmail()));
        updateDto.setPassword(JsonNullable.of(testUser.getPassword()));
        updateDto.setFirstName(JsonNullable.of("Fake name"));
        updateDto.setLastName(JsonNullable.of("Fake lastName"));

        var request = put("/api/users/" + testUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var user = userRepository.findById(testUser.getId()).get();

        assertNotNull(user);
        assertThat(user.getFirstName()).isEqualTo(updateDto.getFirstName().get());
        assertThat(user.getLastName()).isEqualTo(updateDto.getLastName().get());*/
    }

    @Test
    public void testDelete() throws Exception {
        userRepository.save(testUser);

        var request = delete("/api/users/" + testUser.getId());

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(userRepository.existsById(testUser.getId())).isEqualTo(false);
    }




}
