package de.muspellheim.todos.backend.messagehandlers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.muspellheim.todos.backend.adapters.MemoryTodosRepository;
import de.muspellheim.todos.contract.data.Todo;
import de.muspellheim.todos.contract.messages.AddTodosCommand;
import de.muspellheim.todos.contract.messages.CommandStatus;
import de.muspellheim.todos.contract.messages.Success;
import java.util.List;
import org.junit.jupiter.api.Test;

public class AddTodoTests {
  @Test
  void savesNewTodos() {
    testAddTodo(
        List.of(new Todo(1, "Taste JavaScript", true)),
        new AddTodosCommand(("Buy Unicorn")),
        new Success(),
        List.of(new Todo(1, "Taste JavaScript", true), new Todo(2, "Buy Unicorn", false)));
  }

  @Test
  void savesTrimmedTitle() {
    testAddTodo(
        List.of(new Todo(1, "Taste JavaScript", true)),
        new AddTodosCommand(("  Buy Unicorn   ")),
        new Success(),
        List.of(new Todo(1, "Taste JavaScript", true), new Todo(2, "Buy Unicorn", false)));
  }

  @Test
  void doesNothingIfTitleIsEmpty() {
    testAddTodo(
        List.of(new Todo(1, "Taste JavaScript", true)),
        new AddTodosCommand(("  ")),
        new Success(),
        List.of(new Todo(1, "Taste JavaScript", true)));
  }

  void testAddTodo(
      List<Todo> givenTodos,
      AddTodosCommand whenCommand,
      CommandStatus thenStatus,
      List<Todo> thenTodos) {
    var todosRepository = new MemoryTodosRepository();
    var addTodo = new AddTodoCommandHandlerImpl(todosRepository);
    todosRepository.store(givenTodos);

    var status = addTodo.handle(whenCommand);

    assertEquals(thenStatus, status);
    assertEquals(todosRepository.load(), thenTodos);
  }
}
