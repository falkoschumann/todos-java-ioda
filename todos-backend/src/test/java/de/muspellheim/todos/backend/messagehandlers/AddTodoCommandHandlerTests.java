package de.muspellheim.todos.backend.messagehandlers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import de.muspellheim.todos.backend.adapters.MemoryTodosRepository;
import de.muspellheim.todos.contract.data.Todo;
import de.muspellheim.todos.contract.messages.AddTodoCommand;
import de.muspellheim.todos.contract.messages.CommandStatus;
import de.muspellheim.todos.contract.messages.Failure;
import de.muspellheim.todos.contract.messages.Success;
import java.util.List;
import org.junit.jupiter.api.Test;

public class AddTodoCommandHandlerTests {
  @Test
  void savesNewTodos() {
    testAddTodo(
        List.of(new Todo(1, "Taste JavaScript", true)),
        new AddTodoCommand("Buy Unicorn"),
        new Success(),
        List.of(new Todo(1, "Taste JavaScript", true), new Todo(2, "Buy Unicorn", false)));
  }

  @Test
  void savesTrimmedTitle() {
    testAddTodo(
        List.of(new Todo(1, "Taste JavaScript", true)),
        new AddTodoCommand("  Buy Unicorn   "),
        new Success(),
        List.of(new Todo(1, "Taste JavaScript", true), new Todo(2, "Buy Unicorn", false)));
  }

  @Test
  void doesNothingIfTitleIsEmpty() {
    testAddTodo(
        List.of(new Todo(1, "Taste JavaScript", true)),
        new AddTodoCommand("  "),
        new Success(),
        List.of(new Todo(1, "Taste JavaScript", true)));
  }

  void testAddTodo(
      List<Todo> givenTodos,
      AddTodoCommand whenCommand,
      CommandStatus thenStatus,
      List<Todo> thenTodos) {
    var todosRepository = new MemoryTodosRepository();
    var addTodo = new AddTodoCommandHandler(todosRepository);
    todosRepository.store(givenTodos);

    var status = addTodo.handle(whenCommand);

    assertAll(
        () -> assertEquals(thenStatus, status, "status"),
        () -> assertEquals(todosRepository.load(), thenTodos, "todos"));
  }

  @Test
  void fails() {
    var todosRepository = new FailureTodosRepository();
    var addTodo = new AddTodoCommandHandler(todosRepository);

    var whenCommand = new AddTodoCommand("Taste JavaScript");
    var status = addTodo.handle(whenCommand);

    var thenStatus =
        new Failure("Todo \"Taste JavaScript\" could not be added.\n- Something is strange.");
    assertEquals(thenStatus, status, "status");
  }
}
