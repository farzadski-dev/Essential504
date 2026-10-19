package io.github.farzadski.essential504.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import io.github.farzadski.essential504.Adapters.WordSearchAdapter;
import io.github.farzadski.essential504.ObjectBox.Entities.Word;
import io.github.farzadski.essential504.ObjectBox.ObjectBoxStore;
import io.github.farzadski.essential504.R;
import java.util.List;

public class SearchFragment extends Fragment {

  private WordSearchAdapter adapter;

  public SearchFragment() {
    super(R.layout.fragment_search);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    super.onViewCreated(view, savedInstanceState);

    EditText searchInput = view.findViewById(R.id.input_search);
    RecyclerView recyclerView = view.findViewById(R.id.recycler_search_results);

    List<Word> words = ObjectBoxStore.get().boxFor(Word.class).getAll();

    adapter = new WordSearchAdapter(words);
    recyclerView.setAdapter(adapter);

    searchInput.addTextChangedListener(
        new TextWatcher() {

          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {

            adapter.filter(s.toString());
          }

          @Override
          public void afterTextChanged(Editable s) {}
        });

    searchInput.setOnEditorActionListener(
        (textView, actionId, event) -> {
          if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            InputMethodManager inputMethodManager =
                (InputMethodManager)
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);

            if (inputMethodManager != null) {
              inputMethodManager.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
            }

            searchInput.clearFocus();
            return true;
          }

          return false;
        });
  }
}
