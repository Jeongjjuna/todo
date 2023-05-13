package jihun.todo.dto;
import lombok.*;


public class TodoDTO {

  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Getter
  @Setter
  public static class Request {

    private Integer id;

    private String content;
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Getter
  @Setter
  public static class Response {

    private Integer id;

    private String content;
  }

}
