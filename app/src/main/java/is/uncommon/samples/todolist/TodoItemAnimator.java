package is.uncommon.samples.todolist;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;

public class TodoItemAnimator extends DefaultItemAnimator {

  @Override public boolean animateRemove(RecyclerView.ViewHolder holder) {
    if (holder instanceof TodoItemHolder) {
      dispatchRemoveFinished(holder);
      return true;
    }
    return super.animateRemove(holder);
  }
}