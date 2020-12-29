package com.andriichello.tuphics.ui.transitions;

import android.graphics.PointF;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andriichello.tuphics.R;
import com.andriichello.tuphics.types.Pair;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AnchorsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements AnchorInputsListener {
    public static final int ANCHOR_INPUT_TYPE = 100;


    private List<Pair<String, Pair<Float, Float>>> mAnchors = new ArrayList<>();

    private boolean isEditable;
    public AnchorsAdapter(boolean isEditable) {
        this.isEditable = isEditable;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean isEditable) {
        if (this.isEditable != isEditable) {
            this.isEditable = isEditable;
            notifyDataSetChanged();
        }
    }

    public void setAnchors(List<String> anchors) {
        mAnchors.clear();

        if (anchors != null && !anchors.isEmpty()) {
            // removing null values
            anchors.removeIf(Objects::isNull);

            // making sure that list contains only unique values
            for (int i = 0; i < anchors.size(); i++) {
                for (int j = i + 1; j < anchors.size(); j++) {
                    if (anchors.get(i).equals(anchors.get(j))) {
                        anchors.remove(j);
                        j--;
                    }
                }
            }

            for (String anchor : anchors)
                mAnchors.add(new Pair<>(anchor, new Pair<>()));
        }

        notifyDataSetChanged();
    }

    public void setPairs(List<Pair<String, Pair<Float, Float>>> pairs) {
        mAnchors.clear();

        if (pairs != null && !pairs.isEmpty()) {
            // removing null values
            pairs.removeIf(Objects::isNull);

            for (Pair<String, Pair<Float, Float>> pair : pairs) {
                if (!mAnchors.contains(pair))
                    mAnchors.add(pair);
            }
        }

        notifyDataSetChanged();
    }

    public @NonNull List<Pair<String, Pair<Float, Float>>> getPairs() {
        return mAnchors;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ANCHOR_INPUT_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.anchors_recycler_item, parent, false);
            return new AnchorInputsHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AnchorInputsHolder) {
            AnchorInputsHolder anchorHolder = (AnchorInputsHolder) holder;
            Pair<String, Pair<Float, Float>> pair = mAnchors.get(position);

            anchorHolder.mAnchor.setText(pair.first);
            if (pair.second == null) {
                anchorHolder.xInput.setText("");
                anchorHolder.yInput.setText("");
            } else {
                if (pair.second.first == null)
                    anchorHolder.xInput.setText("");
                else
                    anchorHolder.xInput.setText(String.valueOf(pair.second.first));

                if (pair.second.second == null)
                    anchorHolder.yInput.setText("");
                else
                    anchorHolder.yInput.setText(String.valueOf(pair.second.second));
            }

            anchorHolder.mListener = this;

            if (isEditable) {
                anchorHolder.xInput.setEnabled(true);
                anchorHolder.yInput.setEnabled(true);
            } else {
                anchorHolder.xInput.setEnabled(false);
                anchorHolder.yInput.setEnabled(false);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mAnchors.size();
    }

    @Override
    public int getItemViewType(int position) {
        return ANCHOR_INPUT_TYPE;
    }

    @Override
    public void onInputChanged(String text, String anchor, String tag) {
        for (Pair<String, Pair<Float, Float>> pair : mAnchors) {
            if (pair.first.equals(anchor)) {
                if (pair.second == null)
                    pair.second = new Pair<>(null, null);

                if ("x".equals(tag)) {
                    if (text == null || text.length() == 0 || text.matches("[-]?[0-9]{0}"))
                        pair.second.first = null;
                    else
                        pair.second.first = Float.parseFloat(text);
                } else if ("y".equals(tag)) {
                    if (text == null || text.length() == 0 || text.matches("[-]?[0-9]{0}"))
                        pair.second.second = null;
                    else
                        pair.second.second = Float.parseFloat(text);
                }
                break;
            }
        }
    }

    public static class AnchorInputsHolder extends RecyclerView.ViewHolder {
        AnchorInputsListener mListener;

        TextView mAnchor;
        EditText xInput, yInput;

        public AnchorInputsHolder(@NonNull View itemView) {
            super(itemView);

            mAnchor = itemView.findViewWithTag("anchor");
            xInput = itemView.findViewWithTag("x");
            yInput = itemView.findViewWithTag("y");

            xInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (mListener != null)
                        mListener.onInputChanged(s.toString(), mAnchor.getText().toString(), "x");
                }
            });
            yInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (mListener != null)
                        mListener.onInputChanged(s.toString(), mAnchor.getText().toString(), "y");
                }
            });
        }
    }
}
