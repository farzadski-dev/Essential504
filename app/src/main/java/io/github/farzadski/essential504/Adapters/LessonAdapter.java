package io.github.farzadski.essential504.Adapters;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.github.farzadski.essential504.Components.MyCustomCardForLesson;
import io.github.farzadski.essential504.ObjectBox.Entities.Lesson;
import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.VH> {
  private final List<Lesson> lessons;

  public LessonAdapter(List<Lesson> lessons) {
    this.lessons = lessons;
  }

  @NonNull
  @Override
  public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    MyCustomCardForLesson card = new MyCustomCardForLesson(parent.getContext(), new Lesson());
    card.getRootView()
        .setLayoutParams(
            new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    return new VH(card);
  }

  @Override
  public void onBindViewHolder(@NonNull VH holder, int position) {
    holder.card.setLesson(lessons.get(position));
  }

  @Override
  public int getItemCount() {
    return lessons.size();
  }

  public static class VH extends RecyclerView.ViewHolder {
    final MyCustomCardForLesson card;

    VH(MyCustomCardForLesson card) {
      super(card.getRootView());
      this.card = card;
    }
  }
}
