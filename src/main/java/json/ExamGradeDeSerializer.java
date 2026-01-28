package json;

import com.google.gson.*;
import model.ExamGrade;

import java.lang.reflect.Type;

public class ExamGradeDeSerializer implements JsonDeserializer<ExamGrade> {

    @Override
    public ExamGrade deserialize(JsonElement jsonElement,
                                 Type type,
                                 JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return ExamGrade.valueOf(jsonElement.getAsString());
    }
}
