package com.example.trainticket.adapter;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class SqlTimeAdapter extends TypeAdapter<Time> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public void write(JsonWriter out, Time value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            LocalTime localTime = value.toLocalTime();
            String formattedTime = localTime.format(formatter);
            out.value(formattedTime);
        }
    }

    @Override
    public Time read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        } else {
            String timeString = in.nextString();
            LocalTime localTime = LocalTime.parse(timeString, formatter);
            return Time.valueOf(localTime);
        }
    }
}