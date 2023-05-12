package jihun.todo.service;

import jihun.todo.dto.TodoDTO.Request;
import jihun.todo.dto.TodoDTO.Response;
import jihun.todo.exception.BaseException;
import jihun.todo.model.entity.TodoEntity;
import jihun.todo.repository.TodoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

  @InjectMocks
  private TodoService todoService;

  @Mock
  private TodoRepository todoRepository;

  @Spy
  private ModelMapper modelMapper;

  @Nested
  @DisplayName("저장테스트")
  class SaveTest {
    @Test
    @DisplayName("저장성공")
    public void testSave() {
      // given
      Request request = Request.builder()
              .content("영화보기")
              .build();

      TodoEntity entity = TodoEntity.builder()
              .content("영화보기")
              .build();

      // mocking
      when(todoRepository.save(any(TodoEntity.class))).thenReturn(entity);

      // when
      Response actual = todoService.create(request);

      // then
      assertThat(actual.getContent()).isSameAs(entity.getContent());
    }
  }

  @Nested
  @DisplayName("수정테스트")
  class ModifyTest {
    @Test
    @DisplayName("수정성공")
    public void testModify() {
      Integer todoId = 1;
      Request request = Request.builder()
              .content("영화보기")
              .build();

      TodoEntity entity = TodoEntity.builder()
              .id(1)
              .content("영화보기")
              .build();

      when(todoRepository.findById(any())).thenReturn(Optional.of(entity));

      Response actual = todoService.modify(todoId, request);

      assertThat(actual.getContent()).isSameAs(entity.getContent());
    }

    @Test
    @DisplayName("수정시 해당 todo id가 존재하지 않는경우 예외발생")
    public void testModifyException() {
      Integer todoId = 1;
      Request request = Request.builder()
              .id(1)
              .content("영화보기")
              .build();

      when(todoRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

      assertThatCode(() -> todoService.modify(todoId, request))
              .isInstanceOf(BaseException.class)
              .hasMessageContaining("not found");

    }
  }

  @Nested
  @DisplayName("삭제테스트")
  class DeleteTest {
    @Test
    @DisplayName("삭제성공")
    public void testDelete() {
      Integer todoId = 1;
      TodoEntity entity = TodoEntity.builder()
              .id(1)
              .content("영화보기")
              .build();

      when(todoRepository.findById(any(Integer.class))).thenReturn(Optional.of(entity));

      todoService.delete(todoId);

      assertThat(entity.isDeleted()).isTrue();

    }

    @Test
    @DisplayName("삭제시 해당 todo id가 존재하지 않는경우 예외발생")
    public void testDeleteException() {
      Integer todoId = 1;

      // mocking
      when(todoRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

      // then
      assertThatCode(() -> todoService.delete(todoId))
              .isInstanceOf(BaseException.class)
              .hasMessageContaining("not found");
    }
  }

  @Nested
  @DisplayName("조회테스트")
  class FindTest {
    @Test
    @DisplayName("조회성공")
    public void testFind() {
      Integer todoId = 1;

      TodoEntity entity = TodoEntity.builder()
              .id(1)
              .content("영화보기")
              .build();

      // mocking
      when(todoRepository.findById(any(Integer.class))).thenReturn(Optional.of(entity));

      // then
      Response actual = todoService.find(todoId);

      assertThat(actual.getId()).isSameAs(entity.getId());
      assertThat(actual.getContent()).isSameAs(entity.getContent());
    }

    @Test
    @DisplayName("조회시 해당 todo id가 존재하지 않는경우 예외발생")
    public void testFindException() {
      // given
      Integer requestId = 1;

      // mocking
      when(todoRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

      // then
      assertThatCode(() -> todoService.find(requestId))
              .isInstanceOf(BaseException.class)
              .hasMessageContaining("not found");
    }

    @Test
    @DisplayName("전체목록조회 성공")
    public void testFindAll() {
      TodoEntity entity1 = TodoEntity.builder()
              .id(1)
              .content("영화보기")
              .build();
      TodoEntity entity2 = TodoEntity.builder()
              .id(2)
              .content("TV 보기")
              .build();
      List<TodoEntity> todos = new ArrayList<>(List.of(entity1, entity2));
      // mocking
      when(todoRepository.findAll()).thenReturn(todos);

      // then
      List<Response> actual = todoService.findAll();

      assertThat(actual.size()).isEqualTo(2);
    }
  }

}