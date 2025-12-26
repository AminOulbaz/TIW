package json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.ExamGrade;
import java.lang.reflect.Type;

public class ExamGradeSerializer implements JsonSerializer<ExamGrade> {

    @Override
    public JsonElement serialize(
            ExamGrade grade,
            Type type,
            JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        obj.addProperty("label", grade.getLabel());
        return obj;
    }
}
