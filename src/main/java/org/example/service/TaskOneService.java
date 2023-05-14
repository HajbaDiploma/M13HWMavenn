package org.example.service;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.*;
import org.example.dto.Address;
import org.example.dto.GeoData;
import org.example.dto.User;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class TaskOneService {
    public static void main(String[] args) throws IOException {
        User user = taskOneNewUserCreation();
        User userUpdated = taskOneUserUpdate(user);
        taskOneUserDelete(userUpdated);
        List<User> users = taskOneUsersGet();
        taskOneGetUserById(4);
        taskOneGetUserByUsername("Kamren");

    }

    private static List<User> taskOneUsersGet() throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        Request request = new Request.Builder()
                .url("http://jsonplaceholder.typicode.com/users")
                .get()
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();

        List<User> users = jsonTousers(response.body().string());
        System.out.println("all users received = ");
        users.stream()
                .forEach(System.out::println);
        return users;
    }

    private static User taskOneGetUserByUsername(String username) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://jsonplaceholder.typicode.com/users?username=" + username)
                .get()
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();

        List<User> users = jsonTousers(response.body().string());

        System.out.println("next user by username = " + username + " received " + users.get(0));

        return users.get(0);
    }

    private static User taskOneGetUserById(Integer userId) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://jsonplaceholder.typicode.com/users/" + userId)
                .get()
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();

        User user = jsonToUser(response.body().string());

        System.out.println("next user by id " + userId + " " + user + "is received");

        return user;
    }

    private static void taskOneUserDelete(User user) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, userToJson(user));
        Request request = new Request.Builder()
                .url("http://jsonplaceholder.typicode.com/users/" + user.getId())
                .delete()
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        System.out.println("status code for deleting of user with id = " + user.getId() + " = " + response.code());
    }

    private static User taskOneUserUpdate(User user) throws IOException {
        OkHttpClient client = new OkHttpClient();

        user.setName("update user");

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, userToJson(user));
        Request request = new Request.Builder()
                .url("http://jsonplaceholder.typicode.com/users")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();

        return jsonToUser(response.body().string());
    }

    private static User taskOneNewUserCreation() throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, userToJson(createUser()));
        Request request = new Request.Builder()
                .url("http://jsonplaceholder.typicode.com/users")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();

        return jsonToUser(response.body().string());
    }

    private static User createUser() {
        User user = new User();
        user.setName("Anna");
        user.setAddress(createAddress());
        user.setEmail("sfsfs@sds.sdfs");
        user.setUsername("sfsfs");

        return user;
    }

    private static GeoData createGeoData() {
        GeoData geo = new GeoData();
        geo.setLat("00");
        geo.setLng("11");
        return geo;
    }

    private static org.example.dto.Address createAddress() {
        org.example.dto.Address address = new Address();
        address.setCity("Kyiv");
        address.setGeo(createGeoData());
        address.setStreet("River");
        address.setSuite("Apt. 556");
        address.setZipcode("777");
        return address;
    }


    private static String userToJson(User user) {
        Gson gson = new Gson();
        return gson.toJson(user);
    }

    private static User jsonToUser(String user) {
        Type type = TypeToken.getParameterized(User.class).getType();
        return new Gson().fromJson(user, type);
    }

    private static List<User> jsonTousers(String usersStr) {

        Type type = TypeToken.getParameterized(List.class, User.class)
                .getType();

        List<User> usersList = new Gson().fromJson(usersStr, type);
        return usersList;
    }

}
