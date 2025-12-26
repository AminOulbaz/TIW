package json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.ExamGrade;
import model.ExamStatus;
import java.lang.reflect.Type;

public class ExamStatusSerializer implements JsonSerializer<ExamStatus> {

    @Override
    public JsonElement serialize(
            ExamStatus status,
            Type type,
            JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        obj.addProperty("label", status.getLabel());
        return obj;
    }
}
