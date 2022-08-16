package de.muspellheim.todos.backend.messagehandlers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.muspellheim.todos.backend.adapters.MemoryTodosRepository;
import de.muspellheim.todos.contract.data.Todo;
import de.muspellheim.todos.contract.messages.SelectTodosQuery;
import de.muspellheim.todos.contract.messages.SelectTodosQueryResult;
import java.util.List;
import org.junit.jupiter.api.Test;

public class SelectTodosQueryHandlerTests {
  @Test
  void returnsAllTodos() {
    var todosRepository = new MemoryTodosRepository();
    var selectTodos = new SelectTodosQueryHandler(todosRepository);
    var givenTodos =
        List.of(new Todo(1, "Taste JavaScript", true), new Todo(2, "Buy Unicorn", false));
    todosRepository.store(givenTodos);

    var whenQuery = new SelectTodosQuery();
    var result = selectTodos.handle(whenQuery);

    var thenResult =
        new SelectTodosQueryResult(
            List.of(new Todo(1, "Taste JavaScript", true), new Todo(2, "Buy Unicorn", false)));
    assertEquals(thenResult, result);
  }

  @Test
  void fails() {
    var todosRepository = new FailureTodosRepository();
    var selectTodos = new SelectTodosQueryHandler(todosRepository);

    var whenQuery = new SelectTodosQuery();
    var result = selectTodos.handle(whenQuery);

    var thenResult = new SelectTodosQueryResult(List.of());
    assertEquals(thenResult, result);
  }
}
