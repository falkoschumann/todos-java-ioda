package de.muspellheim.todos.backend.messagehandlers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.muspellheim.todos.backend.adapters.MemoryTodosRepository;
import de.muspellheim.todos.contract.data.Todo;
import de.muspellheim.todos.contract.messages.DestroyTodoCommand;
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
    assertEquals(thenStatus, status);
    var thenTodos = List.of(new Todo(1, "Taste JavaScript", true));
    assertEquals(todosRepository.load(), thenTodos);
  }
}
