package is.uncommon.samples.todolist;

import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Custom list to perform {@link List} operations and notify adapter.
 */
public class TodoList {

  final RecyclerView.Adapter adapter;
  List<TodoItem> todoList;

  public TodoList(RecyclerView.Adapter adapter) {
    this.adapter = adapter;
    todoList = new ArrayList<>();
  }

  public void addAll(String[] tasks) {
    for (int i = 0; i < tasks.length; i++) {
      TodoItem item = TodoItem.create(i, tasks[i]);
      add(item);
    }
  }

  /**
   * Add item to the list.
   *
   * @param item {@link TodoItem}.
   */
  public void add(TodoItem item) {
    todoList.add(item);
    adapter.notifyItemInserted(todoList.size() - 1);
  }

  /**
   * Mark item and removed and remove from the list.
   *
   * @param position item position.
   * @param item {@link TodoItem}.
   */
  public void remove(int position, TodoItem item) {
    item.isRemoved(true);
    todoList.remove(item);
    adapter.notifyItemRemoved(position);
  }

  /**
   * Mark item as done and move to end of the list.
   *
   * @param position item position.
   * @param item {@link TodoItem}.
   */
  public void done(int position, TodoItem item) {
    item.isDone(true);
    move(position, todoList.size() - 1);
  }

  /**
   * Mark item as undo and move to the top of the list.
   *
   * @param position item position.
   * @param item {@link TodoItem}.
   */
  public void undo(int position, TodoItem item) {
    item.isDone(false);
    move(position, 0);
  }

  /**
   * Move item fromPosition to newPosition on drag.
   *
   * @param fromPosition start position of the item.
   * @param toPosition end position of the item.
   */
  public void move(int fromPosition, int toPosition) {
    Collections.swap(todoList, fromPosition, toPosition);
    adapter.notifyItemMoved(fromPosition, toPosition);
  }

  /**
   * Size of the list.
   */
  public int size() {
    return todoList.size();
  }

  public TodoItem get(int position) {
    return todoList.get(position);
  }
}
