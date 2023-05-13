package jihun.todo.repository;

import jihun.todo.domain.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TodoRepository extends JpaRepository<TodoEntity, Integer> {
}
