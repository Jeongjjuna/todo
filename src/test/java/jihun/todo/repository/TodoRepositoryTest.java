package jihun.todo.repository;

import jihun.todo.configuration.JpaConfig;
import jihun.todo.model.entity.TodoEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/*\
 @DataJpaTest
 1. DB와 관련된 컴포넌트만 메모리에 로딩 -> Controller x, Service x
 2. 각각의 테스트마다 트랜잭션 처리를해준다(안에 @Transactional 이있음)
 */

/*
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
1. 자동으로 설정된 h2내장 데이터베이스를 사용하지 않도록 설정한다.
2. 자동으로 application.yml 의 db 연결 정보를 확인하고 접속한다.(직 여기선는 테스트용으로 postgresql 을 사용할 수 있다)
 */

@DisplayName("JPA Repository 단위테스트")
@Import(JpaConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TodoRepositoryTest {

  @Autowired
  private TodoRepository todoRepository;

  @Test
  @DisplayName("todo를 저장")
  public void test1() {
    // given
    String content = "책읽기";
    TodoEntity todoEntity = TodoEntity.builder().content(content).build();

    assertThat(todoEntity.getId()).isNull(); // 영속화 전에는 id가 없음

    // when
    TodoEntity savedTodoEntity = todoRepository.save(todoEntity);

    // then
    assertThat(savedTodoEntity.getId()).isNotNull();
    assertThat(savedTodoEntity.getContent()).isEqualTo(content);
    assertThat(savedTodoEntity.getCreateAt()).isNotNull();
    assertThat(savedTodoEntity.getUpdatedAt()).isNotNull();
  }

  @Test
  @DisplayName("todo 리스트 목록 가져오기")
  void test2() {
    // given
    String content1 = "책읽기";
    TodoEntity todoEntity1 = TodoEntity.builder().content(content1).build();
    todoRepository.save(todoEntity1);
    String content2 = "영화보기";
    TodoEntity todoEntity2 = TodoEntity.builder().content(content2).build();
    todoRepository.save(todoEntity2);

    // when
    List<TodoEntity> todos = todoRepository.findAll();

    // then
    assertThat(todos.size()).isEqualTo(2);
  }

  @Test
  @DisplayName("todo 한건보기")
  void test3() {
    // given
    String content = "책읽기";
    TodoEntity todoEntity = TodoEntity.builder().content(content).build();
    TodoEntity savedTodoEntity = todoRepository.save(todoEntity);

    // when
    TodoEntity todo1 = todoRepository.findById(savedTodoEntity.getId()).get();
    /*
     TodoEntity todo2 = todoRepository.findById(2).get();
     -> id는 Transactional 이 primary key 를 초기화 하지 않기 때문에 테스트에서 최대한 사용하지 않도록 한다.
     -> id(오토 인크리먼트)로 사용하고 싶을 떄는 Table 을 drop 시켜야함.
     */
    // then
    assertThat(todo1.getContent()).isEqualTo("책읽기");
  }

  @Test
  @DisplayName("todo 수정하기")
  void test4() {
    // given
    String content = "책읽기";
    String expected = "영화보기";
    TodoEntity todoEntity = TodoEntity.builder().content(content).build();
    TodoEntity savedEntity = todoRepository.save(todoEntity); // 영속성 컨텍스트 저장
    // when
    savedEntity.setContent(expected); // 엔티티값 수정
    todoRepository.save(savedEntity); // 영속성 컨텍스트에 저장

    List<TodoEntity> todos = todoRepository.findAll(); // 데이터 가져오기(영속성 컨텍스트로부터..)
    // then
    assertThat(todos.size()).isEqualTo(1);
    assertThat(todos.get(0).getContent()).isEqualTo("영화보기");
  }


  @Test
  @DisplayName("todo 삭제하기")
  void test5() {
    // given
    String content = "책읽기";
    TodoEntity todoEntity = TodoEntity.builder().content(content).build();
    todoRepository.save(todoEntity);

    // when
    TodoEntity todo = todoRepository.findAll().get(0);
    todo.setDeleted(true);
    TodoEntity savedEntity = todoRepository.save(todo);

    // then
    assertThat(savedEntity.isDeleted()).isTrue();
  }

  @Test
  @DisplayName("삭제되지 않은 todo 리스트 목록 가져오기")
  void test6() {
    // given
    String content1 = "책읽기";
    TodoEntity todoEntity1 = TodoEntity.builder().content(content1).build();
    todoRepository.save(todoEntity1);

    String content2 = "영화보기";
    TodoEntity todoEntity2 = TodoEntity.builder().content(content2).build();
    todoRepository.save(todoEntity2);

    String content3 = "게임하기";
    TodoEntity todoEntity3 = TodoEntity.builder().content(content3).build();
    todoRepository.save(todoEntity3);

    // when
    List<TodoEntity> todos = todoRepository.findAll();
    TodoEntity todo1 = todos.get(0);
    TodoEntity todo2 = todos.get(1);
    todoRepository.delete(todo1);
    todoRepository.delete(todo2);
    List<TodoEntity> notRemovedTodos = todoRepository.findAll();

    // then
    assertThat(notRemovedTodos.size()).isEqualTo(1);
    assertThat(notRemovedTodos.get(0).getContent()).isEqualTo("게임하기");
  }
}