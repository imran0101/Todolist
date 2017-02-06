package is.uncommon.samples.todolist;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import timber.log.Timber;

public class TodoList extends SortedListAdapterCallback<TodoItem> {

  SortedList<TodoItem> todoList;

  public TodoList(RecyclerView.Adapter adapter) {
    super(adapter);
    todoList = new SortedList<TodoItem>(TodoItem.class, this);
  }

  @Override public int compare(TodoItem o1, TodoItem o2) {
    return o1.compare(o2);
  }

  @Override public boolean areContentsTheSame(TodoItem oldItem, TodoItem newItem) {
    return oldItem.areContentsTheSame(newItem);
  }

  @Override public boolean areItemsTheSame(TodoItem item1, TodoItem item2) {
    return item1.areItemsTheSame(item2);
  }

  public void addAll(String[] tasks) {
    for (int i = 0; i < tasks.length; i++) {
      TodoItem item = TodoItem.create(i, tasks[i]);
      add(item);
    }
  }

  public void add(TodoItem item) {
    Timber.d("add: %s", item);
    todoList.add(item);
  }

  public void remove(TodoItem item) {
    todoList.remove(item);
  }

  public SortedList<TodoItem> items() {
    return todoList;
  }

  public int size() {
    return todoList.size();
  }

  public TodoItem get(int position) {
    return todoList.get(position);
  }
}
