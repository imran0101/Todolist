package is.uncommon.samples.todolist;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Notify adapter on item selection for drag.
 */
public class TodoItemTouchCallback extends ItemTouchHelper.SimpleCallback {

  ItemTouchHelperAdapter itemTouchHelperAdapter;

  public TodoItemTouchCallback(ItemTouchHelperAdapter itemTouchHelperAdapter) {
    super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.START | ItemTouchHelper.END);
    this.itemTouchHelperAdapter = itemTouchHelperAdapter;
  }

  public void adapter(ItemTouchHelperAdapter adapter) {
    this.itemTouchHelperAdapter = adapter;
  }

  @Override public boolean isLongPressDragEnabled() {
    return true;
  }

  @Override public boolean isItemViewSwipeEnabled() {
    return false;
  }

  @Override public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
      RecyclerView.ViewHolder target) {
    itemTouchHelperAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
    return true;
  }

  @Override public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

  }

  @Override public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
    super.clearView(recyclerView, viewHolder);

    if (viewHolder instanceof TodoItemHolder) {
      ((TodoItemHolder) viewHolder).onUnselected();
    }
  }

  @Override public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
    super.onSelectedChanged(viewHolder, actionState);
    if (viewHolder == null) {
      return;
    }
    if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
      if (viewHolder instanceof TodoItemHolder) {
        ((TodoItemHolder) viewHolder).onSelected();
      }
    }
  }
}