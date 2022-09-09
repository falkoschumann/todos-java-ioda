package de.muspellheim.todos.backend.messagehandlers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import de.muspellheim.todos.backend.adapters.MemoryTodosRepository;
import de.muspellheim.todos.contract.data.Todo;
import de.muspellheim.todos.contract.messages.DestroyTodoCommand;
import de.muspellheim.todos.contract.messages.Failure;
import de.muspellheim.todos.contract.messages.Success;
import java.util.List;
import org.junit.jupiter.api.Test;

public class DestroyTodoCommandHandlerTests {
  @Test
  void destroysATodo() {
    var todosRepository = new MemoryTodosRepository();
    var destroyTodo = new DestroyTodoCommandHandler(todosRepository);
    var givenTodos =
        List.of(new Todo(1, "Taste JavaScript", true), new Todo(2, "Buy Unicorn", false));
    todosRepository.store(givenTodos);

    var whenCommand = new DestroyTodoCommand(2);
    var status = destroyTodo.handle(whenCommand);

    var thenStatus = new Success();
    var thenTodos = List.of(new Todo(1, "Taste JavaScript", true));
    assertAll(
        () -> assertEquals(thenStatus, status, "status"),
        () -> assertEquals(todosRepository.load(), thenTodos, "todos"));
  }

  @Test
  void fails() {
    var todosRepository = new FailureTodosRepository();
    var destroyTodo = new DestroyTodoCommandHandler(todosRepository);

    var whenCommand = new DestroyTodoCommand(1);
    var status = destroyTodo.handle(whenCommand);

    var thenStatus = new Failure("Todo \"1\" could not be destroyed.\n- Something is strange.");
    assertEquals(thenStatus, status, "status");
  }
}
