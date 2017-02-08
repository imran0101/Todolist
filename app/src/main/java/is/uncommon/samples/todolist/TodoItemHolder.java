package is.uncommon.samples.todolist;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
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
import timber.log.Timber;

public class TodoItemHolder extends RecyclerView.ViewHolder {

  int[] rotationAngles = new int[] { 7, 6, 5, 4, 3, -3, -4, -5, -6, -7 };
  @BindView(R.id.card_todo) CardView cardTodo;
  @BindView(R.id.view_type) View viewType;
  @BindView(R.id.view_type_end) View viewTypeEnd;
  @BindView(R.id.view_overlay) View viewOverlay;
  @BindView(R.id.text_todo) TextView textTodo;
  @BindView(R.id.btn_remove) View btnRemove;
  @BindView(R.id.btn_done) View btnDone;

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
    Timber.d("bind: %s", item);
    textTodo.setText(item.task());
    btnRemove.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        remove(item, callback, getAdapterPosition());
      }
    });

    if (!item.isDone()) {
      btnDone.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          btnDone.setEnabled(false);
          done(item, callback, getAdapterPosition());
        }
      });
    } else {
      btnDone.setEnabled(false);
    }
  }

  public void onSelected() {
    int i = new Random().nextInt(rotationAngles.length);

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

    ValueAnimator rotateAnimator = ValueAnimator.ofFloat(0, rotationAngles[i]);
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

  private void done(final TodoItem item, final TodoItemAdapterCallback callback,
      final int adapterPosition) {
    Timber.d("done: %s %s", item, adapterPosition);
    if (adapterPosition == RecyclerView.NO_POSITION) {
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

    ValueAnimator scaleRightAnimator = ValueAnimator.ofFloat(0, 1f);
    scaleRightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
        float animatedValue = (float) valueAnimator.getAnimatedValue();
        int width = itemWidth - viewType.getWidth();
        float scale = width * animatedValue;
        ViewCompat.setScaleX(viewType, scale);
        ViewCompat.setTranslationX(viewType, scale);
        Timber.d("onAnimationUpdate: %s %s", cardTodo.getWidth(), scale);
      }
    });
    scaleRightAnimator.setInterpolator(new FastOutSlowInInterpolator());

    scaleRightAnimator.setStartDelay(0L);
    scaleRightAnimator.setDuration(300L);

    ValueAnimator scaleLeftAnimator = ValueAnimator.ofFloat(1f, 0);

    scaleLeftAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
        float animatedValue = (float) valueAnimator.getAnimatedValue();
        int width = itemWidth - viewType.getWidth();
        float scale = width * animatedValue;
        ViewCompat.setScaleX(viewType, scale);
        Timber.d("onAnimationUpdate: %s %s", cardTodo.getWidth(), scale);
      }
    });

    scaleLeftAnimator.setInterpolator(new FastOutSlowInInterpolator());
    scaleLeftAnimator.setStartDelay(0L);
    scaleLeftAnimator.setDuration(300L);
    scaleLeftAnimator.setStartDelay(200L);

    scaleLeftAnimator.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animation) {

      }

      @Override public void onAnimationEnd(Animator animation) {
        cardTodo.setScaleX(1f);
        cardTodo.setScaleY(1f);
        viewTypeEnd.setVisibility(View.VISIBLE);
        viewType.setVisibility(View.INVISIBLE);
        viewOverlay.setVisibility(View.VISIBLE);
        callback.done(getAdapterPosition(), item);
      }

      @Override public void onAnimationCancel(Animator animation) {

      }

      @Override public void onAnimationRepeat(Animator animation) {

      }
    });

    AnimatorSet animatorSet = new AnimatorSet();
    AnimatorSet te = new AnimatorSet();
    te.playTogether(scaleRightAnimator, scaleLeftAnimator);
    te.setStartDelay(100L);
    animatorSet.playTogether(elevateAnimator, te);
    animatorSet.start();
  }

  private void remove(final TodoItem item, final TodoItemAdapterCallback callback,
      final int adapterPosition) {
    Timber.d("remove: %s %s", item, adapterPosition);

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