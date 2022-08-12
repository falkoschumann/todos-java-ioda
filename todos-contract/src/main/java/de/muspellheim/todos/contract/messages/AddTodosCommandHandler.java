package de.muspellheim.todos.contract.messages;

@FunctionalInterface
public interface AddTodosCommandHandler {
  CommandStatus handle(AddTodosCommand command);
}
