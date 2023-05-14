package org.example.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.example.dto.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class TaskThreeService {
    public static void main(String[] args) throws IOException {
        getListOfActiveTasksForUser(5);
    }

    private static void getListOfActiveTasksForUser(int userId) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://jsonplaceholder.typicode.com/users/" + userId + "/todos")
                .get()
                .build();

        Response response = client.newCall(request).execute();

        String stringBody = response.body().string();

        List<Task> tasks = jsonToTasks(stringBody);

        System.out.println("next open tasks received for user with id = " + userId);
        tasks.stream()
                .filter(t -> t.isCompleted() == false)
                .forEach(System.out::println);
    }

    private static List<Task> jsonToTasks(String tasksStr) {

        Type type = TypeToken.getParameterized(List.class, Task.class)
                .getType();

        List<Task> tasks = new Gson().fromJson(tasksStr, type);
        return tasks;
    }
}
