package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.ExamGrade;
import model.ExamStatus;

public class ExamResultsGsonFactory {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ExamGrade.class, new ExamGradeSerializer())
            .registerTypeAdapter(ExamStatus.class, new ExamStatusSerializer())
            .registerTypeAdapter(ExamGrade.class, new ExamGradeSerializer())
            .registerTypeAdapter(ExamStatus.class, new ExamStatusDeSerializer())
            .create();

    public static Gson getGson() {
        return gson;
    }
}

