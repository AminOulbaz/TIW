package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.User;

public class UserGsonFactory {
    private static final Gson gson =  new GsonBuilder()
            .registerTypeAdapter(User.class, new UserSerializer())
            .create();

    public static Gson getGson() {
        return gson;
    }
}
