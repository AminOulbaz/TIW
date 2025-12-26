package json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import model.ExamGrade;
import model.ExamStatus;

import java.lang.reflect.Type;

public class ExamStatusDeSerializer implements JsonDeserializer<ExamStatus> {

    @Override
    public ExamStatus deserialize(JsonElement jsonElement,
                                 Type type,
                                 JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return ExamStatus.valueOf(jsonElement.getAsString());
    }
}
