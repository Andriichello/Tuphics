package com.andriichello.tuphics.ui.adapters;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.andriichello.tuphics.R;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class CheckboxAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<AbstractMap.SimpleEntry<String, Boolean>> mItems = new ArrayList<>();

    private int mActiveColor, mNonactiveColor;
    public CheckboxAdapter(@NonNull Resources resources, Resources.Theme theme) {
        mActiveColor = resources.getColor(R.color.white, theme);
        mNonactiveColor = resources.getColor(R.color.light_gray, theme);
    }

    public void setData(List<String> items, String active) {
        mItems.clear();

        if (items != null && items.size() > 0) {
            for (String item : items) {
                if (item == null || item.length() == 0)
                    continue;

                if (item.equals(active))
                    mItems.add(new AbstractMap.SimpleEntry<>(item, true));
                else
                    mItems.add(new AbstractMap.SimpleEntry<>(item, false));
            }
        }
        notifyDataSetChanged();
    }

    public String getFirst(boolean state) {
        for (int i = 0; i < mItems.size(); i++) {
            if (mItems.get(i).getValue() == state)
                return mItems.get(i).getKey();
        }
        return null;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkbox_item, parent, false);
        return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemHolder) {
            ItemHolder item = (ItemHolder) holder;

            item.mText.setText(mItems.get(position).getKey());
            if (mItems.get(position).getValue())
                mLastActiveItem = item;
            item.setState(mItems.get(position).getValue());
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    private ItemHolder mLastActiveItem = null;
    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView mText;
        private ImageView mIcon;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            mText = itemView.findViewById(R.id.Checkbox_Item_Text);
            mIcon = itemView.findViewById(R.id.Checkbox_Item_Icon);


            itemView.setOnClickListener(v -> {
                int newPos = getAdapterPosition();
                if (newPos < 0 || newPos >= mItems.size())
                    return;

                boolean state = mItems.get(newPos).getValue();
                if (!state) {
                    if (mLastActiveItem != null) {
                        int oldPos = mLastActiveItem.getAdapterPosition();
                        if (oldPos >= 0 && oldPos < mItems.size()) {
                            mItems.get(oldPos).setValue(false);
                            mLastActiveItem.setState(false);
                        }
                    }

                    mItems.get(newPos).setValue(true);
                    mLastActiveItem = ItemHolder.this;
                    mLastActiveItem.setState(true);
                }

            });
        }

        public void setState(boolean isActive) {
            if (isActive) {
                mText.setTextColor(mActiveColor);
                mIcon.setColorFilter(mActiveColor);
                mIcon.setImageResource(R.drawable.ic_checkbox_circle_active);
            } else {
                mText.setTextColor(mNonactiveColor);
                mIcon.setColorFilter(mNonactiveColor);
                mIcon.setImageResource(R.drawable.ic_checkbox_circle_nonactive);
            }
        }
    }
}
