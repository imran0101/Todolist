package is.uncommon.samples.todolist;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public class TodoTodoAdapter extends RecyclerView.Adapter<TodoItemHolder>
    implements ItemTouchHelperAdapter, TodoItemAdapterCallback {

  TodoList todoList = new TodoList(this);

  private String[] todoArray = new String[] {
      "Send that Package", "Call dad", "Finish design work", "Pick up laundry",
      "Pay electricity bill", "Book flight tickets"
  };

  public TodoTodoAdapter() {
    todoList.addAll(todoArray);
  }

  @Override public TodoItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return TodoItemHolder.create(parent);
  }

  @Override public void onBindViewHolder(TodoItemHolder holder, int position) {
    holder.bind(todoList.get(position), this);
  }

  @Override public int getItemCount() {
    return todoList.size();
  }

  @Override public void onItemMove(int fromPosition, int toPosition) {
    //move items in the list.
    todoList.move(fromPosition, toPosition);
  }

  @Override public void onItemDismiss(int position) {

  }

  public void reset() {
    todoList.items().clear();
    todoList.addAll(todoArray);
  }

  @Override public void remove(int position, TodoItem item) {
    if (item == null) {
      return;
    }
    todoList.remove(position, item);
  }

  @Override public void done(int position, TodoItem item) {
    if (position == RecyclerView.NO_POSITION) {
      return;
    }
    todoList.done(position, item);
  }
}