package com.aeh.api.utils;

public class Pair<T, U> {
    private T first;
    private U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public T getFirst() {
        return first;
    }

    public void setSecond(U second) {
        this.second = second;
    }

    public U getSecond() {
        return second;
    }
}
