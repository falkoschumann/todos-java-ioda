package de.muspellheim.todos.backend.messagehandlers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import de.muspellheim.todos.backend.adapters.MemoryTodosRepository;
import de.muspellheim.todos.contract.data.Todo;
import de.muspellheim.todos.contract.messages.ClearCompletedCommand;
import de.muspellheim.todos.contract.messages.Failure;
import de.muspellheim.todos.contract.messages.Success;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ClearCompletedCommandHandlerTests {
  @Test
  void removesCompletedTodos() {
    var todosRepository = new MemoryTodosRepository();
    var clearCompleted = new ClearCompletedCommandHandler(todosRepository);
    var givenTodos =
        List.of(new Todo(1, "Taste JavaScript", true), new Todo(2, "Buy Unicorn", false));
    todosRepository.store(givenTodos);

    var whenCommand = new ClearCompletedCommand();
    var status = clearCompleted.handle(whenCommand);

    var thenStatus = new Success();
    var thenTodos = List.of(new Todo(2, "Buy Unicorn", false));
    assertAll(
        () -> assertEquals(thenStatus, status),
        () -> assertEquals(todosRepository.load(), thenTodos));
  }

  @Test
  void fails() {
    var todosRepository = new FailureTodosRepository();
    var clearCompleted = new ClearCompletedCommandHandler(todosRepository);

    var whenCommand = new ClearCompletedCommand();
    var status = clearCompleted.handle(whenCommand);

    var thenStatus = new Failure("Could not clear completed todos.\n- something is strange");
    assertEquals(thenStatus, status);
  }
}
