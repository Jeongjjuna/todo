package jihun.todo.service;

import jihun.todo.dto.TodoDTO.Request;
import jihun.todo.dto.TodoDTO.Response;
import jihun.todo.exception.BaseException;
import jihun.todo.exception.ResultType;
import jihun.todo.model.entity.TodoEntity;
import jihun.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService {

  private final ModelMapper modelMapper;

  private final TodoRepository todoRepository;

  @Transactional
  public Response create(Request request) {
    TodoEntity entity = modelMapper.map(request, TodoEntity.class);

    TodoEntity created = todoRepository.save(entity);

    return modelMapper.map(created, Response.class);
  }

  @Transactional
  public Response modify(Request request) {
    Integer todoId = request.getId();

    TodoEntity entity = todoRepository.findById(todoId).orElseThrow(() ->
            new BaseException(ResultType.NOT_FOUND));

    entity.setContent(request.getContent());
    todoRepository.save(entity);

    return modelMapper.map(entity, Response.class);
  }

  @Transactional
  public void delete(Integer todoId) {
    TodoEntity entity = todoRepository.findById(todoId).orElseThrow(() ->
            new BaseException(ResultType.NOT_FOUND));

    entity.setDeleted(true);

    todoRepository.save(entity);
  }

  @Transactional
  public Response find(Integer todoId) {
    TodoEntity entity = todoRepository.findById(todoId).orElseThrow(() ->
            new BaseException(ResultType.NOT_FOUND));

    return modelMapper.map(entity, Response.class);
  }

  @Transactional
  public List<Response> findAll() {
    List<TodoEntity> todos = todoRepository.findAll();

    return todos.stream()
            .map(todo -> modelMapper.map(todo, Response.class))
            .collect(Collectors.toList());
  }
}
