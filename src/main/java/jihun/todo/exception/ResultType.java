package jihun.todo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultType {

  SUCCESS("0000", "success"),

  SYSTEM_ERROR("9000", "system error"),

  NOT_FOUND("4000", "not found");

  private final String code;
  private final String desc;
}