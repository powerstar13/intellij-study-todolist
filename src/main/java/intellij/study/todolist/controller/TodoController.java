package intellij.study.todolist.controller;

import intellij.study.todolist.model.TodoEntity;
import intellij.study.todolist.model.TodoRequest;
import intellij.study.todolist.model.TodoResponse;
import intellij.study.todolist.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/")
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<TodoResponse> create(@RequestBody TodoRequest request) {

        log.info("CREATE");

        // 유효성 검사
        if (StringUtils.isBlank(request.getTitle())) return ResponseEntity.badRequest().build();
        if (request.getOrder() == null) request.setOrder(0L);
        if (request.getCompleted() == null) request.setCompleted(false);

        TodoEntity result = todoService.add(request);

        return ResponseEntity.ok(new TodoResponse(result));
    }

    @GetMapping("{id}")
    public ResponseEntity<TodoResponse> readOne(@PathVariable Long id) {

        log.info("READ ONE");

        TodoEntity result = todoService.searchById(id);

        return ResponseEntity.ok(new TodoResponse(result));
    }

    @GetMapping
    public ResponseEntity<List<TodoResponse>> readAll() {

        log.info("READ ALL");

        List<TodoEntity> result = todoService.searchAll();

        return ResponseEntity.ok(
            result.stream().map(TodoResponse::new)
                .collect(Collectors.toList())
        );
    }

    @PatchMapping("{id}")
    public ResponseEntity<TodoResponse> update(@PathVariable Long id, @RequestBody TodoRequest request) {

        log.info("UPDATE");

        TodoEntity result = todoService.updateById(id, request);

        return ResponseEntity.ok(new TodoResponse(result));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteOne(@PathVariable Long id) {

        log.info("DELETE");

        todoService.deleteById(id);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll() {

        log.info("DELETE ALL");

        todoService.deleteAll();

        return ResponseEntity.ok().build();
    }
}
