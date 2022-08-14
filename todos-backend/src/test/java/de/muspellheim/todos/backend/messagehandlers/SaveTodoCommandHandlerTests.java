package de.muspellheim.todos.backend.messagehandlers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.muspellheim.todos.backend.adapters.MemoryTodosRepository;
import de.muspellheim.todos.contract.data.Todo;
import de.muspellheim.todos.contract.messages.CommandStatus;
import de.muspellheim.todos.contract.messages.SaveTodoCommand;
import de.muspellheim.todos.contract.messages.Success;
import java.util.List;
import org.junit.jupiter.api.Test;

public class SaveTodoCommandHandlerTests {
  @Test
  void changesTodosTitle() {
    testSaveTodo(
        List.of(new Todo(1, "Taste JavaScript", true), new Todo(2, "Buy Unicorn", false)),
        new SaveTodoCommand(1, "Taste TypeScript"),
        new Success(),
        List.of(new Todo(1, "Taste TypeScript", true), new Todo(2, "Buy Unicorn", false)));
  }

  @Test
  void trimsTitle() {
    testSaveTodo(
        List.of(new Todo(1, "Taste JavaScript", true), new Todo(2, "Buy Unicorn", false)),
        new SaveTodoCommand(1, "  Taste TypeScript   "),
        new Success(),
        List.of(new Todo(1, "Taste TypeScript", true), new Todo(2, "Buy Unicorn", false)));
  }

  @Test
  void destroysTodoIfTitleIsEmpty() {
    testSaveTodo(
        List.of(new Todo(1, "Taste JavaScript", true), new Todo(2, "Buy Unicorn", false)),
        new SaveTodoCommand(1, "  "),
        new Success(),
        List.of(new Todo(2, "Buy Unicorn", false)));
  }

  void testSaveTodo(
      List<Todo> givenTodos,
      SaveTodoCommand whenCommand,
      CommandStatus thenStatus,
      List<Todo> thenTodos) {
    var todosRepository = new MemoryTodosRepository();
    var addTodo = new SaveTodoCommandHandler(todosRepository);
    todosRepository.store(givenTodos);

    var status = addTodo.handle(whenCommand);

    assertEquals(thenStatus, status);
    assertEquals(todosRepository.load(), thenTodos);
  }
}
