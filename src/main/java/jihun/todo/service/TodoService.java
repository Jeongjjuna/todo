package jihun.todo.service;

import jihun.todo.model.TodoDto;
import jihun.todo.model.entity.TodoEntity;
import jihun.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService {

  private final TodoRepository todoRepository;

  @Transactional
  public TodoDto create(TodoDto todoDto) {
    TodoEntity savedTodo = todoRepository.save(TodoEntity.of(todoDto));
    return TodoDto.from(savedTodo);
  }

  @Transactional
  public TodoDto modify(TodoDto todoDto) {
    Integer todoId = todoDto.getId();

    TodoEntity todoEntity = todoRepository.findById(todoId).orElseThrow(() ->
            new IllegalArgumentException());

    todoEntity.setContent(todoDto.getContent());

    return TodoDto.from(todoRepository.save(todoEntity));
  }

  @Transactional
  public void delete(Integer todoId) {
    TodoEntity todoEntity = todoRepository.findById(todoId).orElseThrow(() ->
            new IllegalArgumentException());

    todoRepository.delete(todoEntity);
  }

  @Transactional
  public TodoDto find(Integer todoId) {
    TodoEntity todoEntity = todoRepository.findById(todoId).orElseThrow(() ->
            new IllegalArgumentException());
    return TodoDto.from(todoEntity);
  }

  @Transactional
  public List<TodoDto> findList() {
    List<TodoEntity> todos = todoRepository.findAll();
    return todos.stream().map(TodoDto::from).collect(Collectors.toList());
  }
}
