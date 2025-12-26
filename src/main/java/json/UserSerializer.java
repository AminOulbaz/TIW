package json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.User;
import java.lang.reflect.Type;

public class UserSerializer implements JsonSerializer<User> {

    @Override
    public JsonElement serialize(
            User user,
            Type type,
            JsonSerializationContext context) {

        JsonObject json = new JsonObject();

        json.addProperty("username", user.getUsername());
        json.addProperty("email", user.getEmail());

        if (user.getRole() != null) {
            json.addProperty("role", user.getRole().toString());
        }

        return json;
    }
}

