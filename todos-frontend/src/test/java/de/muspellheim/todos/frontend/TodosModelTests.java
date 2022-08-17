package de.muspellheim.todos.frontend;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import de.muspellheim.todos.contract.data.Todo;
import java.util.List;
import org.junit.jupiter.api.Test;

public class TodosModelTests {
  @Test
  void isEmpty() {
    testTodosModel(List.of(), Filter.ALL, false, List.of(), false, 0, false);
  }

  @Test
  void showsAll() {
    testTodosModel(
        List.of(new Todo(1, "Taste JavaScript", true), new Todo(2, "Buy Unicorn", false)),
        Filter.ALL,
        true,
        List.of(new Todo(1, "Taste JavaScript", true), new Todo(2, "Buy Unicorn", false)),
        false,
        1,
        true);
  }

  @Test
  void showsActive() {
    testTodosModel(
        List.of(new Todo(1, "Taste JavaScript", true), new Todo(2, "Buy Unicorn", false)),
        Filter.ACTIVE,
        true,
        List.of(new Todo(2, "Buy Unicorn", false)),
        false,
        1,
        true);
  }

  @Test
  void showsCompleted() {
    testTodosModel(
        List.of(new Todo(1, "Taste JavaScript", true), new Todo(2, "Buy Unicorn", false)),
        Filter.COMPLETED,
        true,
        List.of(new Todo(1, "Taste JavaScript", true)),
        false,
        1,
        true);
  }

  @Test
  void containsOnlyActive() {
    testTodosModel(
        List.of(new Todo(1, "Taste JavaScript", false), new Todo(2, "Buy Unicorn", false)),
        Filter.ALL,
        true,
        List.of(new Todo(1, "Taste JavaScript", false), new Todo(2, "Buy Unicorn", false)),
        false,
        2,
        false);
  }

  @Test
  void containsOnlyCompleted() {
    testTodosModel(
        List.of(new Todo(1, "Taste JavaScript", true), new Todo(2, "Buy Unicorn", true)),
        Filter.ALL,
        true,
        List.of(new Todo(1, "Taste JavaScript", true), new Todo(2, "Buy Unicorn", true)),
        true,
        0,
        true);
  }

  void testTodosModel(
      List<Todo> whenTodos,
      Filter whenFilter,
      boolean thenExistsTodos,
      List<Todo> thenShownTodos,
      boolean thenIsAllCompleted,
      int thenActiveCount,
      boolean thenExistsCompleted) {
    var model = new TodosModel();

    model.updateTodos(whenTodos);
    model.setFilter(whenFilter);

    assertAll(
        () -> assertEquals(thenExistsTodos, model.existsTodos(), "exists todos"),
        () -> assertEquals(thenShownTodos, model.shownTodos(), "shown todos"),
        () -> assertEquals(thenIsAllCompleted, model.isAllCompleted(), "is all completed"),
        () -> assertEquals(thenActiveCount, model.activeCount(), "active count"),
        () -> assertEquals(thenExistsCompleted, model.existsCompleted(), "exists completed"));
  }
}
