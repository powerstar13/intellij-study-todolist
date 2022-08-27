package intellij.study.todolist.repository;

import intellij.study.todolist.model.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, Long> { // JpaRepository의 제네릭에는 엔터티와 ID를 전달하면 된다.
}
