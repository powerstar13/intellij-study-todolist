package intellij.study.todolist.service;

import intellij.study.todolist.model.TodoEntity;
import intellij.study.todolist.model.TodoRequest;
import intellij.study.todolist.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    /**
     * todo 리스트 목록에 아이템을 추가
     * @param request: 추가할 아이템 정보
     * @return TodoEntity: 추가된 레퍼런스
     */
    public TodoEntity add(TodoRequest request) {

        TodoEntity todoEntity = TodoEntity.builder()
            .title(request.getTitle())
            .order(request.getOrder())
            .completed(request.getCompleted())
            .build();

        return todoRepository.save(todoEntity);
    }

    /**
     * todo 리스트 목록 중 특정 아이템을 조회
     * @param id: 식별키
     * @return TodoEntity: 조회된 레퍼런스
     */
    public TodoEntity searchById(Long id) {

        return todoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    /**
     * todo 리스트 전체 목록을 조회
     * @return TodoEntity: 조회된 레퍼런스 목록
     */
    public List<TodoEntity> searchAll() {
        return todoRepository.findAll();
    }

    /**
     * todo 리스트 목록 중 특정 아이템을 수정
     * @param id: 식별키
     * @return TodoEntity: 수정된 레퍼런스
     */
    public TodoEntity updateById(Long id, TodoRequest request) {

        TodoEntity todoEntity = this.searchById(id);

        if (StringUtils.isNotBlank(request.getTitle())) {
            todoEntity.modifyTitle(request.getTitle());
        }

        if(request.getOrder() != null) {
            todoEntity.modifyOrder(request.getOrder());
        }

        if (request.getCompleted() != null) {
            todoEntity.modifyCompleted(request.getCompleted());
        }

        return todoRepository.save(todoEntity);
    }

    /**
     * todo 리스트 목록 중 특정 아이템을 삭제
     * @param id: 식별키
     */
    public void deleteById(Long id) {

        TodoEntity todoEntity = todoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        todoRepository.delete(todoEntity);
    }

    /**
     * todo 리스트 전체 목록을 삭제
     */
    public void deleteAll() {
        todoRepository.deleteAll();
    }
}
