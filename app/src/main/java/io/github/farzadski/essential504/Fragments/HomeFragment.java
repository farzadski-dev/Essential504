package io.github.farzadski.essential504.Fragments;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import io.github.farzadski.essential504.Adapters.LessonAdapter;
import io.github.farzadski.essential504.ObjectBox.Entities.Lesson;
import io.github.farzadski.essential504.ObjectBox.ObjectBoxStore;
import io.github.farzadski.essential504.R;

public class HomeFragment extends Fragment {
  private RecyclerView list;

  public HomeFragment() {
    super(R.layout.fragment_home);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    list = view.findViewById(R.id.recycler_lessons);
    list.setAdapter(new LessonAdapter(ObjectBoxStore.get().boxFor(Lesson.class).getAll()));
  }

  @Override
  public void onResume() {
    super.onResume();
    list.setAdapter(new LessonAdapter(ObjectBoxStore.get().boxFor(Lesson.class).getAll()));
  }
}
