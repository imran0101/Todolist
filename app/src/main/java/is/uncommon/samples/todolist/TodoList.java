package is.uncommon.samples.todolist;

import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

  public void add(TodoItem item) {
    todoList.add(item);
    adapter.notifyItemInserted(todoList.size() - 1);
  }

  public void remove(int position, TodoItem item) {
    item.isRemoved(true);
    todoList.remove(item);
    adapter.notifyItemRemoved(position);
  }

  public void done(int position, TodoItem item) {
    item.isDone(true);
    move(position, todoList.size() - 1);
  }

  public void move(int fromPosition, int toPosition) {
    Collections.swap(todoList, fromPosition, toPosition);
    adapter.notifyItemMoved(fromPosition, toPosition);
  }

  public List<TodoItem> items() {
    return todoList;
  }

  public int size() {
    return todoList.size();
  }

  public TodoItem get(int position) {
    return todoList.get(position);
  }
}
