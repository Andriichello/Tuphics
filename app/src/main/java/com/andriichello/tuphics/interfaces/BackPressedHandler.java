package com.andriichello.tuphics.interfaces;

public interface BackPressedHandler {
    // if true then fragment handles that back press
    // if false then the caller has to handle it
    boolean onBackPressed();
}
