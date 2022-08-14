package de.muspellheim.todos.backend.messagehandlers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.muspellheim.todos.backend.adapters.MemoryTodosRepository;
import de.muspellheim.todos.contract.data.Todo;
import de.muspellheim.todos.contract.messages.ClearCompletedCommand;
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
    assertEquals(thenStatus, status);
    var thenTodos = List.of(new Todo(2, "Buy Unicorn", false));
    assertEquals(todosRepository.load(), thenTodos);
  }
}
