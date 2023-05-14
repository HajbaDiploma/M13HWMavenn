package org.example.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.example.dto.Comment;
import org.example.dto.Post;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskTwoService {
    public static void main(String[] args) throws IOException {
        int userId = 5;
        int lastPostIdForUser = getLastPostIdForUser(userId);
        List<Comment> comments = getCommentsForPost(lastPostIdForUser);
        createFileWithComments(comments, userId, lastPostIdForUser);
    }

    private static void sout() {
        System.out.println("AHAHAHAHAHAH");
    }

    private static int getLastPostIdForUser(int userId) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://jsonplaceholder.typicode.com/users/" + userId + "/posts")
                .get()
                .build();

        Response response = client.newCall(request).execute();

        String stringBody = response.body().string();

        List<Post> posts = jsonToPosts(stringBody);

        System.out.println("next posts received for user with id = " + userId);
        posts.stream().forEach(System.out::println);

        int maxId = -1;
        for (Post p : posts) {
            if (Integer.valueOf(p.getId()) > maxId) {
                maxId = Integer.valueOf(p.getId());
            }
        }

        System.out.println("last post with id = " + maxId);

        return maxId;
    }

    private static void createFileWithComments(List<Comment> comments, int userId, int postId) throws IOException {

        File jsonFile = new File("user-" + userId + "-post-" + postId + "-comments.json");

        FileOutputStream fos = new FileOutputStream(jsonFile);
        String json = commentsToJson(comments);
        fos.write(json.getBytes(StandardCharsets.UTF_8));

        fos.flush();
        fos.close();
    }

    private static List<Comment> getCommentsForPost(int postId) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://jsonplaceholder.typicode.com/posts/" + postId + "/comments")
                .get()
                .build();

        Response response = client.newCall(request).execute();

        String stringBody = response.body().string();

        List<Comment> comments = jsonToComments(stringBody);

        System.out.println("next comments received for post with id = " + postId);
        comments.stream().forEach(System.out::println);


        return comments;
    }

    private static List<Post> jsonToPosts(String postsStr) {

        Type type = TypeToken.getParameterized(List.class, Post.class)
                .getType();

        List<Post> posts = new Gson().fromJson(postsStr, type);
        return posts;
    }

    private static List<Comment> jsonToComments(String commentStr) {

        Type type = TypeToken.getParameterized(List.class, Comment.class)
                .getType();

        List<Comment> posts = new Gson().fromJson(commentStr, type);
        return posts;
    }

    private static String commentsToJson(List<Comment> comments) {
        Gson gson = new Gson().newBuilder().setPrettyPrinting().create();
        return gson.toJson(comments);

    }
}
