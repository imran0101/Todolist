package is.uncommon.samples.todolist;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.Random;

public class TodoItemHolder extends RecyclerView.ViewHolder {

  int[] rotationAngles = new int[] { 7, 6, 5, 4, 3, -3, -4, -5, -6, -7 };
  int[] itemTypeColors = new int[] {
      Color.argb(255, 244, 143, 177), Color.argb(255, 179, 157, 219),
      Color.argb(255, 129, 212, 250), Color.argb(255, 128, 203, 196),
      Color.argb(255, 174, 213, 129), Color.argb(255, 255, 183, 77)
  };
  @BindView(R.id.card_todo) CardView cardTodo;
  @BindView(R.id.view_type_start) View viewTypeStart;
  @BindView(R.id.view_type_end) View viewTypeEnd;
  @BindView(R.id.view_overlay) View viewOverlay;
  @BindView(R.id.view_overlay_translate) View viewOverLayTranslate;
  @BindView(R.id.text_todo) TextView textTodo;
  @BindView(R.id.btn_remove) View btnRemove;
  @BindView(R.id.btn_done) View btnDone;

  private boolean isAnimating = false;

  public TodoItemHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
  }

  public static TodoItemHolder create(ViewGroup viewGroup) {
    View view =
        LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_todo, viewGroup, false);
    return new TodoItemHolder(view);
  }

  public void bind(final TodoItem item, final TodoItemAdapterCallback callback) {
    setTypeColor();
    textTodo.setText(item.task());

    btnRemove.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        remove(item, callback, getAdapterPosition());
      }
    });

    btnDone.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (item.isDone()) {
          undo(item, callback, getAdapterPosition());
        } else {
          done(item, callback, getAdapterPosition());
        }
      }
    });
  }

  private void setTypeColor() {
    int index = new Random().nextInt(itemTypeColors.length);

    viewOverLayTranslate.setBackgroundColor(itemTypeColors[index]);
    viewTypeStart.setBackgroundColor(itemTypeColors[index]);
    viewTypeEnd.setBackgroundColor(itemTypeColors[index]);
  }

  /**
   * On Item selected rotate and animate itemview from 0 to a random angle.
   */
  public void onSelected() {
    int index = new Random().nextInt(rotationAngles.length);

    ValueAnimator elevateAnimator = ValueAnimator.ofFloat(1f, 1.03f);
    elevateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        float animatedValue = (float) animation.getAnimatedValue();
        ViewCompat.setScaleX(cardTodo, animatedValue);
        ViewCompat.setScaleY(cardTodo, animatedValue);
      }
    });

    elevateAnimator.setInterpolator(new FastOutSlowInInterpolator());
    elevateAnimator.setDuration(200);

    ValueAnimator rotateAnimator = ValueAnimator.ofFloat(0, rotationAngles[index]);
    rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
        float animatedValue = (float) valueAnimator.getAnimatedValue();
        ViewCompat.setRotation(itemView, animatedValue);
      }
    });
    rotateAnimator.setInterpolator(new FastOutSlowInInterpolator());
    rotateAnimator.setDuration(200);

    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playTogether(elevateAnimator, rotateAnimator);
    animatorSet.start();
  }

  /**
   * On Item unselected rotate and animate itemview from current angle to 0.
   */
  public void onUnselected() {

    ValueAnimator elevateAnimator = ValueAnimator.ofFloat(1.03f, 1f);
    elevateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        float animatedValue = (float) animation.getAnimatedValue();
        ViewCompat.setScaleX(cardTodo, animatedValue);
        ViewCompat.setScaleY(cardTodo, animatedValue);
      }
    });

    elevateAnimator.setInterpolator(new FastOutSlowInInterpolator());
    elevateAnimator.setDuration(200);

    ValueAnimator rotateAnimator = ValueAnimator.ofFloat(itemView.getRotation(), 0);
    rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
        float animatedValue = (float) valueAnimator.getAnimatedValue();
        ViewCompat.setRotation(itemView, animatedValue);
      }
    });
    rotateAnimator.setInterpolator(new FastOutSlowInInterpolator());
    rotateAnimator.setDuration(200);

    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playTogether(rotateAnimator, elevateAnimator);
    animatorSet.start();
  }

  /**
   * On Done. Animate the view and push it to the bottom of the list.
   *
   * @param item {@link TodoItem} item done.
   * @param callback {@link TodoItemAdapterCallback} callback.
   * @param adapterPosition position in adapter.
   */
  private void done(final TodoItem item, final TodoItemAdapterCallback callback,
      final int adapterPosition) {
    if (adapterPosition == RecyclerView.NO_POSITION) {
      return;
    }

    if (isAnimating) {
      return;
    }

    final int itemWidth = itemView.getWidth() - viewTypeStart.getWidth();

    ValueAnimator elevateAnimator = ValueAnimator.ofFloat(1f, 1.03f);
    elevateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        float animatedValue = (float) animation.getAnimatedValue();
        ViewCompat.setScaleX(cardTodo, animatedValue);
        ViewCompat.setScaleY(cardTodo, animatedValue);
      }
    });

    elevateAnimator.setInterpolator(new FastOutSlowInInterpolator());
    elevateAnimator.setStartDelay(0L);
    elevateAnimator.setDuration(300L);

    ValueAnimator translateAnimator = ValueAnimator.ofFloat(-1f, 1f);
    translateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
        float animatedValue = (float) valueAnimator.getAnimatedValue();
        ViewCompat.setTranslationX(viewOverLayTranslate, animatedValue * itemWidth);
      }
    });

    translateAnimator.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animator) {
        ViewCompat.setTranslationX(viewOverLayTranslate, 0);
        viewOverLayTranslate.setVisibility(View.VISIBLE);
        viewTypeStart.setVisibility(View.INVISIBLE);
      }

      @Override public void onAnimationEnd(Animator animator) {
        cardTodo.setScaleX(1f);
        cardTodo.setScaleY(1f);
        viewTypeEnd.setVisibility(View.VISIBLE);
        viewOverlay.setVisibility(View.VISIBLE);
        viewOverLayTranslate.setVisibility(View.INVISIBLE);
        callback.done(getAdapterPosition(), item);
      }

      @Override public void onAnimationCancel(Animator animator) {

      }

      @Override public void onAnimationRepeat(Animator animator) {

      }
    });
    translateAnimator.setDuration(800L);
    translateAnimator.setStartDelay(50L);

    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playTogether(elevateAnimator, translateAnimator);
    animatorSet.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animator) {
        isAnimating = true;
      }

      @Override public void onAnimationEnd(Animator animator) {
        isAnimating = false;
      }

      @Override public void onAnimationCancel(Animator animator) {

      }

      @Override public void onAnimationRepeat(Animator animator) {

      }
    });
    animatorSet.start();
  }

  /**
   * On Undo item. Animate itemview and push it to top of the list.
   *
   * @param item {@link TodoItem} item undo.
   * @param callback {@link TodoItemAdapterCallback} callback.
   * @param adapterPosition position in adapter.
   */
  private void undo(final TodoItem item, final TodoItemAdapterCallback callback,
      final int adapterPosition) {
    if (adapterPosition == RecyclerView.NO_POSITION) {
      return;
    }

    if (isAnimating) {
      return;
    }

    final int itemWidth = cardTodo.getWidth();

    ValueAnimator elevateAnimator = ValueAnimator.ofFloat(1f, 1.03f);
    elevateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        float animatedValue = (float) animation.getAnimatedValue();
        ViewCompat.setScaleX(cardTodo, animatedValue);
        ViewCompat.setScaleY(cardTodo, animatedValue);
      }
    });

    elevateAnimator.setInterpolator(new FastOutSlowInInterpolator());
    elevateAnimator.setStartDelay(0L);
    elevateAnimator.setDuration(300L);

    ValueAnimator translateAnimator = ValueAnimator.ofFloat(1f, -1f);
    translateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
        float animatedValue = (float) valueAnimator.getAnimatedValue();
        ViewCompat.setTranslationX(viewOverLayTranslate, animatedValue * itemWidth);
      }
    });

    translateAnimator.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animator) {
        viewOverLayTranslate.setVisibility(View.VISIBLE);
        viewTypeEnd.setVisibility(View.INVISIBLE);
      }

      @Override public void onAnimationEnd(Animator animator) {
        cardTodo.setScaleX(1f);
        cardTodo.setScaleY(1f);
        viewTypeStart.setVisibility(View.VISIBLE);
        viewOverlay.setVisibility(View.GONE);
        viewOverLayTranslate.setVisibility(View.INVISIBLE);
        callback.undo(getAdapterPosition(), item);
      }

      @Override public void onAnimationCancel(Animator animator) {

      }

      @Override public void onAnimationRepeat(Animator animator) {

      }
    });
    translateAnimator.setDuration(800L);
    translateAnimator.setStartDelay(50L);

    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playTogether(elevateAnimator, translateAnimator);
    animatorSet.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animator) {
        isAnimating = true;
      }

      @Override public void onAnimationEnd(Animator animator) {
        isAnimating = false;
      }

      @Override public void onAnimationCancel(Animator animator) {

      }

      @Override public void onAnimationRepeat(Animator animator) {

      }
    });
    animatorSet.start();
  }

  /**
   * Remove item from the list.
   *
   * @param item {@link TodoItem} item to be removed.
   * @param callback {@link TodoItemAdapterCallback} callback.
   * @param adapterPosition position in adapter.
   */
  private void remove(final TodoItem item, final TodoItemAdapterCallback callback,
      final int adapterPosition) {

    final ValueAnimator elevateAnimator = ValueAnimator.ofFloat(1f, 1.03f);
    elevateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        float animatedValue = (float) animation.getAnimatedValue();
        ViewCompat.setTranslationZ(cardTodo, animatedValue);
        ViewCompat.setScaleX(cardTodo, animatedValue);
        ViewCompat.setScaleY(cardTodo, animatedValue);
      }
    });
    elevateAnimator.setInterpolator(new FastOutSlowInInterpolator());
    elevateAnimator.setDuration(300);

    ValueAnimator valueAnimator = ValueAnimator.ofFloat(itemView.getScaleX(), 0);
    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
        float animatedValue = (float) valueAnimator.getAnimatedValue();
        ViewCompat.setScaleX(itemView, animatedValue);
      }
    });
    valueAnimator.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animation) {

      }

      @Override public void onAnimationEnd(Animator animation) {
        cardTodo.setScaleX(1f);
        cardTodo.setScaleY(1f);
        callback.remove(adapterPosition, item);
      }

      @Override public void onAnimationCancel(Animator animation) {

      }

      @Override public void onAnimationRepeat(Animator animation) {

      }
    });
    valueAnimator.setInterpolator(new FastOutSlowInInterpolator());
    valueAnimator.setDuration(300);

    AnimatorSet animatorSet = new AnimatorSet();
    animatorSet.playSequentially(elevateAnimator, valueAnimator);
    animatorSet.start();
  }
}