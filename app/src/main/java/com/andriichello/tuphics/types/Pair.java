package com.andriichello.tuphics.types;

public class Pair<V1, V2> {
    public V1 first;
    public V2 second;

    public Pair() {
        this.first = null;
        this.second = null;
    }

    public Pair(V1 first, V2 second) {
        this.first = first;
        this.second = second;
    }
}
