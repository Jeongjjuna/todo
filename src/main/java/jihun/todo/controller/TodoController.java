package jihun.todo.controller;

import jihun.todo.dto.ResponseDTO;
import jihun.todo.dto.TodoDTO.Request;
import jihun.todo.dto.TodoDTO.Response;
import jihun.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/todo")
public class TodoController extends AbstractController {

  private final TodoService todoService;

  @PostMapping
  public ResponseDTO<Response> create(@RequestBody Request request) {
    return ok(todoService .create(request));
  }

  @GetMapping("/{todoId}")
  public ResponseDTO<Response> find(@PathVariable Integer todoId) {
    return ok(todoService.find(todoId));
  }

  @GetMapping
  public ResponseDTO<List<Response>> findAll() {
    return ok(todoService.findAll());
  }

  @PutMapping("/{todoId}")
  public ResponseDTO<Response> modify(@PathVariable Integer todoId, @RequestBody Request request) {
    return ok(todoService.modify(todoId, request));
  }

  @DeleteMapping("/{todoId}")
  public ResponseDTO<Response> delete(@PathVariable Integer todoId) {
    todoService.delete(todoId);
    return ok();
  }

}
