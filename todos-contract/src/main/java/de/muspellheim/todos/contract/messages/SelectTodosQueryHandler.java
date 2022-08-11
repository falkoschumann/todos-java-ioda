package de.muspellheim.todos.contract.messages;

@FunctionalInterface
public interface SelectTodosQueryHandler {
  SelectTodosQueryResult handle(SelectTodosQuery query);
}
