package intellij.study.todolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import intellij.study.todolist.model.TodoEntity;
import intellij.study.todolist.model.TodoRequest;
import intellij.study.todolist.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TodoService todoService;

    private TodoEntity expected;

    @BeforeEach
    void setup() {
        this.expected = TodoEntity.builder()
            .id(123L)
            .title("TEST TITLE")
            .order(0L)
            .completed(false)
            .build();
    }

    @Test
    void create() throws Exception {

        when(todoService.add(any(TodoRequest.class)))
            .then(invocation -> {
                TodoRequest request = invocation.getArgument(0, TodoRequest.class);
                return TodoEntity.builder()
                    .id(this.expected.getId())
                    .title(request.getTitle())
                    .order(this.expected.getOrder())
                    .completed(this.expected.getCompleted())
                    .build();
            });

        TodoRequest request = TodoRequest.builder()
            .title("ANY TITLE")
            .build();

        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(request);

        this.mvc
            .perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("ANY TITLE"));
    }
}