package io.github.farzadski.essential504.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.github.farzadski.essential504.Components.MyCustomCardForWord;
import io.github.farzadski.essential504.ObjectBox.Entities.Word;
import io.github.farzadski.essential504.RandomColor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WordSearchAdapter extends RecyclerView.Adapter<WordSearchAdapter.WordViewHolder> {
  private final List<Word> allWords = new ArrayList<>();
  private final List<Word> filteredWords = new ArrayList<>();

  public WordSearchAdapter(List<Word> words) {
    allWords.addAll(words);
    filteredWords.addAll(words);
  }

  @NonNull
  @Override
  public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    MyCustomCardForWord card = new MyCustomCardForWord(context);
    card.setSearchMode();
    card.setLayoutParams(
        new RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

    return new WordViewHolder(card);
  }

  @Override
  public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
    Word word = filteredWords.get(position);
    holder.card.setVisibility(View.VISIBLE);
    holder.card.applySearchWord(word, RandomColor.get("80", "90", "100"));
  }

  @Override
  public int getItemCount() {
    return filteredWords.size();
  }

  public void filter(String query) {
    filteredWords.clear();
    String normalizedQuery = query.trim().toLowerCase(Locale.ROOT);

    if (normalizedQuery.isEmpty()) {
      filteredWords.addAll(allWords);
    } else {
      for (Word word : allWords) {
        String name = word.Word == null ? "" : word.Word.toLowerCase(Locale.ROOT);

        if (name.contains(normalizedQuery)) {
          filteredWords.add(word);
        }
      }
    }

    notifyDataSetChanged();
  }

  public static class WordViewHolder extends RecyclerView.ViewHolder {
    final MyCustomCardForWord card;

    WordViewHolder(@NonNull MyCustomCardForWord card) {
      super(card);
      this.card = card;
    }
  }
}
