package com.andriichello.tuphics.events;

import androidx.annotation.NonNull;

import com.andriichello.tuphics.types.Category;

public class CategorySelectedEvent {
    private Category mCategory;

    public CategorySelectedEvent(@NonNull Category category) {
        this.mCategory = category;
    }

    public Category getCategory() {
        return mCategory;
    }

    public void setCategory(@NonNull Category category) {
        this.mCategory = category;
    }
}
