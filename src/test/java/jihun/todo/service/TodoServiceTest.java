package jihun.todo.service;

import jihun.todo.model.TodoDto;
import jihun.todo.model.entity.TodoEntity;
import jihun.todo.repository.TodoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class TodoServiceTest {

  @Autowired
  private TodoService todoService;

  @MockBean
  private TodoRepository todoRepository;


  @Nested
  @DisplayName("저장테스트")
  class SaveTest {
    @Test
    @DisplayName("저장성공")
    public void testSave() {
      // given
      String content = "영화보기";
      TodoDto todoDto = TodoDto.builder()
              .content(content)
              .build();

      // mocking
      when(todoRepository.save(any())).thenReturn(TodoEntity.of(todoDto));

      // then
      assertThatCode(() -> todoService.create(todoDto))
              .doesNotThrowAnyException();
    }
  }

  @Nested
  @DisplayName("수정테스트")
  class ModifyTest {
    public TodoDto createRequestDto() {
      Integer requestId = 1;
      String requestContent = "영화보기";
      return TodoDto.builder()
              .id(requestId)
              .content(requestContent)
              .build();
    }

    public TodoEntity createEntity() {
      Integer id = 1;
      String content = "밥먹기";
      return TodoEntity.builder()
              .id(id)
              .content(content)
              .build();
    }

    @Test
    @DisplayName("수정성공")
    public void testModify() {
      // given
      TodoDto requestTodoDto = createRequestDto();
      TodoEntity foundEntity = createEntity();

      // mocking
      when(todoRepository.findById(any())).thenReturn(Optional.of(foundEntity));
      when(todoRepository.save(any())).thenReturn(foundEntity);

      // when
      TodoDto updated = todoService.modify(requestTodoDto);

      // then
      assertThat(updated.getContent()).isEqualTo("영화보기");
    }

    @Test
    @DisplayName("수정시 해당 todo가 존재하지 않는경우 예외발생")
    public void testModifyException() {
      // given
      TodoDto requestTodoDto = createRequestDto();

      // mocking
      when(todoRepository.findById(any())).thenReturn(Optional.empty());

      // then
      assertThatCode(() -> todoService.modify(requestTodoDto))
              .isInstanceOf(IllegalArgumentException.class);
    }
  }

  @Nested
  @DisplayName("삭제테스트")
  class DeleteTest {
    @Test
    @DisplayName("삭제성공")
    public void testDelete() {
      // given
      Integer requestId = 1;

      // mocking
      when(todoRepository.findById(any())).thenReturn(Optional.of(mock(TodoEntity.class)));

      // then
      assertThatCode(() -> todoService.delete(requestId))
              .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("삭제시 해당 todo가 존재하지 않는경우 예외발생")
    public void testDeleteException() {
      // given
      Integer requestId = 1;

      // mocking
      when(todoRepository.findById(any())).thenReturn(Optional.empty());

      // then
      assertThatCode(() -> todoService.delete(requestId))
              .isInstanceOf(IllegalArgumentException.class);
    }
  }

  @Nested
  @DisplayName("조회테스트")
  class FindTest {
    @Test
    @DisplayName("조회성공")
    public void testFind() {
      // given
      Integer requestId = 1;
      String requestContent = "영화보기";
      TodoDto requestTodoDto = TodoDto.builder()
              .id(requestId)
              .content(requestContent)
              .build();
      TodoEntity findTodoEntity = TodoEntity.of(requestTodoDto);

      // mocking
      when(todoRepository.findById(any())).thenReturn(Optional.of(findTodoEntity));

      // then
      assertThatCode(() -> todoService.find(requestId))
              .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("조회시 해당 todo가 존재하지 않는경우 예외발생")
    public void testFindException() {
      // given
      Integer requestId = 1;

      // mocking
      when(todoRepository.findById(any())).thenReturn(Optional.empty());

      // then
      assertThatCode(() -> todoService.find(requestId))
              .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("전체목록조회 성공")
    public void testFindAll() {
      // mocking
      when(todoRepository.findAll()).thenReturn(Collections.emptyList());

      // then
      assertThatCode(() -> todoService.findList())
              .doesNotThrowAnyException();
    }
  }






}