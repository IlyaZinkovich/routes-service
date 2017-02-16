package com.routes.service.entities;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

public class Route {

    private Long id;
    private Place from;
    private Place to;
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String source;

    public Route() {
    }

    public Route(Place from, Place to, LocalDate date, String source) {
        this.from = from;
        this.to = to;
        this.date = date;
        this.source = source;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Place getFrom() {
        return from;
    }

    public Place getTo() {
        return to;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getSource() {
        return source;
    }
}