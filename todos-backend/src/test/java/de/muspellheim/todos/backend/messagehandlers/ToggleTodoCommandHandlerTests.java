package de.muspellheim.todos.backend.messagehandlers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import de.muspellheim.todos.backend.adapters.MemoryTodosRepository;
import de.muspellheim.todos.contract.data.Todo;
import de.muspellheim.todos.contract.messages.CommandStatus;
import de.muspellheim.todos.contract.messages.Failure;
import de.muspellheim.todos.contract.messages.Success;
import de.muspellheim.todos.contract.messages.ToggleTodoCommand;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ToggleTodoCommandHandlerTests {
  @Test
  void activatesATodo() {
    testToggleTodo(
        List.of(new Todo(1, "Taste JavaScript", true), new Todo(2, "Buy Unicorn", false)),
        new ToggleTodoCommand(1),
        new Success(),
        List.of(new Todo(1, "Taste JavaScript", false), new Todo(2, "Buy Unicorn", false)));
  }

  @Test
  void completesATodo() {
    testToggleTodo(
        List.of(new Todo(1, "Taste JavaScript", true), new Todo(2, "Buy Unicorn", false)),
        new ToggleTodoCommand(2),
        new Success(),
        List.of(new Todo(1, "Taste JavaScript", true), new Todo(2, "Buy Unicorn", true)));
  }

  void testToggleTodo(
      List<Todo> givenTodos,
      ToggleTodoCommand whenCommand,
      CommandStatus thenStatus,
      List<Todo> thenTodos) {
    var todosRepository = new MemoryTodosRepository();
    var toggleTodo = new ToggleTodoCommandHandler(todosRepository);
    todosRepository.store(givenTodos);

    var status = toggleTodo.handle(whenCommand);

    assertAll(
        () -> assertEquals(thenStatus, status, "status"),
        () -> assertEquals(todosRepository.load(), thenTodos, "todos"));
  }

  @Test
  void fails() {
    var todosRepository = new FailureTodosRepository();
    var toggleTodo = new ToggleTodoCommandHandler(todosRepository);

    var whenCommand = new ToggleTodoCommand(1);
    var status = toggleTodo.handle(whenCommand);

    var thenStatus = new Failure("Todo \"1\" could not be toggled.\n- something is strange");
    assertEquals(thenStatus, status, "status");
  }
}
