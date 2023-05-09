package jihun.todo.repository;

import jihun.todo.model.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TodoRepository extends JpaRepository<TodoEntity, Integer> {
  @Modifying
  @Query("UPDATE TodoEntity t SET t.deleted = true WHERE t = :todo")
  void delete(@Param("todo") TodoEntity todoEntity);
}
