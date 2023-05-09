package jihun.todo.model;

import jihun.todo.model.entity.TodoEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TodoDto {
  private Integer id;
  private String content;

  public static TodoDto from(TodoEntity todoEntity) {
    return TodoDto.builder()
            .id(todoEntity.getId())
            .content(todoEntity.getContent())
            .build();
  }
}
