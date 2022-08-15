package de.muspellheim.todos.backend.messagehandlers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.muspellheim.todos.backend.adapters.MemoryTodosRepository;
import de.muspellheim.todos.contract.data.Todo;
import de.muspellheim.todos.contract.messages.CommandStatus;
import de.muspellheim.todos.contract.messages.Success;
import de.muspellheim.todos.contract.messages.ToggleAllCommand;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ToggleAllCommandHandlerTests {
  @Test
  void setAllTodosCompleted() {
    testToggleAll(
        List.of(new Todo(1, "Taste JavaScript", true), new Todo(2, "Buy Unicorn", false)),
        new ToggleAllCommand(true),
        new Success(),
        List.of(new Todo(1, "Taste JavaScript", true), new Todo(2, "Buy Unicorn", true)));
  }

  @Test
  void setAllTodosActive() {
    testToggleAll(
        List.of(new Todo(1, "Taste JavaScript", true), new Todo(2, "Buy Unicorn", false)),
        new ToggleAllCommand(false),
        new Success(),
        List.of(new Todo(1, "Taste JavaScript", false), new Todo(2, "Buy Unicorn", false)));
  }

  void testToggleAll(
      List<Todo> givenTodos,
      ToggleAllCommand whenCommand,
      CommandStatus thenStatus,
      List<Todo> thenTodos) {
    var todosRepository = new MemoryTodosRepository();
    var toggleTodo = new ToggleAllCommandHandler(todosRepository);
    todosRepository.store(givenTodos);

    var status = toggleTodo.handle(whenCommand);

    assertEquals(thenStatus, status);
    assertEquals(todosRepository.load(), thenTodos);
  }
}
