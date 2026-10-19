package io.github.farzadski.essential504.ObjectBox;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.farzadski.essential504.ObjectBox.Entities.Lesson;
import io.github.farzadski.essential504.ObjectBox.Entities.Word;
import io.objectbox.Box;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DatabaseSeeder {
  private static final String TAG = "DatabaseSeeder";
  private static final String ASSET_PATH = "_words.json";

  private DatabaseSeeder() {}

  public static void seed(Context context) {
    Box<Lesson> lessonBox = lessonBox();
    if (isSeeded(lessonBox)) return;
    seedFromJson(context, lessonBox);
  }

  private static boolean isSeeded(Box<Lesson> lessonBox) {
    if (lessonBox.count() == 0) return false;
    Log.d(TAG, "DB already seeded. Skipping.");
    return true;
  }

  private static void seedFromJson(Context context, Box<Lesson> lessonBox) {
    List<LessonJson> lessons = loadLessons(context);
    if (isEmpty(lessons)) return;
    insertAll(lessons);
    logCount(lessonBox);
  }

  private static boolean isEmpty(List<LessonJson> lessons) {
    if (lessons != null && !lessons.isEmpty()) return false;
    Log.w(TAG, "Parsed JSON is empty.");
    return true;
  }

  private static void logCount(Box<Lesson> lessonBox) {
    Log.d(TAG, "Inserted " + lessonBox.count() + " lessons.");
  }

  private static List<LessonJson> loadLessons(Context context) {
    try {
      return parseLessons(readAsset(context));
    } catch (Exception e) {
      Log.e(TAG, "Failed to load lessons", e);
      return null;
    }
  }

  private static List<LessonJson> parseLessons(String json) {
    Type type = new TypeToken<List<LessonJson>>() {}.getType();
    return new Gson().fromJson(json, type);
  }

  private static void insertAll(List<LessonJson> lessons) {
    Set<Long> lessonIds = new HashSet<>();
    Set<Long> wordIds = new HashSet<>();
    ObjectBoxStore.get().runInTx(() -> insertAllInTx(lessons, lessonIds, wordIds));
  }

  private static void insertAllInTx(
      List<LessonJson> lessons, Set<Long> lessonIds, Set<Long> wordIds) {
    for (LessonJson lj : lessons) insertLesson(lj, lessonIds, wordIds);
  }

  private static void insertLesson(LessonJson lj, Set<Long> lessonIds, Set<Long> wordIds) {
    if (!lessonIds.add(lj.Id)) return;
    Lesson lesson = toLesson(lj);
    lessonBox().put(lesson);
    attachWords(lesson, lj, wordIds);
  }

  private static Lesson toLesson(LessonJson lj) {
    Lesson lesson = new Lesson();
    lesson.Id = lj.Id;
    lesson.LessonNumber = lj.LessonNumber;
    return lesson;
  }

  private static void attachWords(Lesson lesson, LessonJson lj, Set<Long> wordIds) {
    if (lj.Words == null || lj.Words.isEmpty()) return;
    List<Word> words = toWords(lj, wordIds);
    saveWords(words);
    linkWords(lesson, words);
  }

  private static List<Word> toWords(LessonJson lj, Set<Long> wordIds) {
    List<Word> words = new ArrayList<>();
    for (LessonJson.WordJson wj : lj.Words) {
      if (wordIds.add(wj.Id)) words.add(toWord(wj));
    }
    return words;
  }

  private static Word toWord(LessonJson.WordJson wj) {
    Word word = new Word();
    word.Id = wj.Id;
    word.LessonId = wj.LessonId;
    word.LessonNumber = wj.LessonNumber;
    word.Word = capitalize(wj.Word);
    word.Meaning = capitalize(wj.Meaning);
    return word;
  }

  private static void saveWords(List<Word> words) {
    if (words.isEmpty()) return;
    ObjectBoxStore.get().boxFor(Word.class).put(words);
  }

  private static void linkWords(Lesson lesson, List<Word> words) {
    Box<Lesson> lessonBox = lessonBox();
    Lesson persisted = lessonBox.get(lesson.Id);
    persisted.Words.addAll(words);
    lessonBox.put(persisted);
  }

  private static Box<Lesson> lessonBox() {
    return ObjectBoxStore.get().boxFor(Lesson.class);
  }

  private static String readAsset(Context context) throws Exception {
    try (InputStream is = context.getAssets().open(ASSET_PATH)) {
      return readAll(is);
    }
  }

  private static String readAll(InputStream is) throws Exception {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
      return readLines(reader);
    }
  }

  private static String readLines(BufferedReader reader) throws Exception {
    StringBuilder sb = new StringBuilder();
    for (String line; (line = reader.readLine()) != null; ) {
      sb.append(line);
    }
    return sb.toString();
  }

  public static String capitalize(String str) {
    if (str == null || str.isEmpty()) return str;
    return str.substring(0, 1).toUpperCase() + str.substring(1);
  }
}
