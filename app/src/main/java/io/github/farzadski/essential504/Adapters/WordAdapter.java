package io.github.farzadski.essential504.Adapters;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.github.farzadski.essential504.Components.MyCustomCardForWord;
import io.github.farzadski.essential504.ObjectBox.Entities.Word;
import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.VH> {

  private final List<Word> words;
  private final int color;

  public WordAdapter(List<Word> words, int color) {
    this.words = words;
    this.color = color;
  }

  @NonNull
  @Override
  public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    MyCustomCardForWord card = new MyCustomCardForWord(parent.getContext(), new Word(), color);
    card.setLayoutParams(
        new RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    return new VH(card);
  }

  @Override
  public void onBindViewHolder(@NonNull VH holder, int position) {
    holder.card.setWord(words.get(position));
  }

  @Override
  public int getItemCount() {
    return words.size();
  }

  public static class VH extends RecyclerView.ViewHolder {
    final MyCustomCardForWord card;

    VH(MyCustomCardForWord card) {
      super(card);
      this.card = card;
    }
  }
}
