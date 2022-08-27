package intellij.study.todolist.service;

import intellij.study.todolist.model.TodoEntity;
import intellij.study.todolist.model.TodoRequest;
import intellij.study.todolist.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Test
    void add() {

        when(todoRepository.save(any(TodoEntity.class)))
            .then(AdditionalAnswers.returnsFirstArg());

        TodoRequest expected = new TodoRequest();
        expected.setTitle("Test Title");

        TodoEntity actual = todoService.add(expected);

        assertEquals(expected.getTitle(), actual.getTitle());
    }

    @Test
    void searchById() {

        TodoEntity entity = TodoEntity.builder()
            .id(123L)
            .title("TITLE")
            .order(0L)
            .completed(false)
            .build();
        Optional<TodoEntity> optional = Optional.of(entity);

        given(todoRepository.findById(anyLong()))
            .willReturn(optional);

        TodoEntity actual = todoService.searchById(123L);

        TodoEntity expected = optional.get();

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getOrder(), actual.getOrder());
        assertEquals(expected.getCompleted(), actual.getCompleted());
    }

    @Test
    void searchByIdFailed() {

        given(todoRepository.findById(anyLong()))
            .willReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> todoService.searchById(123L));
    }
}