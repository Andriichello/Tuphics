package com.andriichello.tuphics.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andriichello.tuphics.R;
import com.andriichello.tuphics.events.CategorySelectedEvent;
import com.andriichello.tuphics.interfaces.BackPressedHandler;
import com.andriichello.tuphics.types.Category;

import org.greenrobot.eventbus.EventBus;

public class MainFragment extends Fragment
    implements BackPressedHandler {

    private MainViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        View toolbar = view.findViewById(R.id.Main_Toolbar);
        if (toolbar != null) {
            toolbar.findViewById(R.id.Toolbar_Title).setVisibility(View.VISIBLE);
            ((TextView) toolbar.findViewById(R.id.Toolbar_Title)).setText(R.string.app_name);

            ImageView settingsIcon = toolbar.findViewById(R.id.Toolbar_RightIcon);
//            settingsIcon.setVisibility(View.VISIBLE);
            settingsIcon.setImageResource(R.drawable.ic_settings);
            settingsIcon.setColorFilter(getResources().getColor(R.color.white, null));
            settingsIcon.setOnClickListener(v -> {
                // TODO: open settings fragment
            });
        }

        // TODO: create and set up categories recycler
        view.findViewById(R.id.Main_Fractals).setOnClickListener(v -> {
            EventBus.getDefault().post(new CategorySelectedEvent(Category.Fractals));
        });

        view.findViewById(R.id.Main_ImageColoring).setOnClickListener(v -> {
            EventBus.getDefault().post(new CategorySelectedEvent(Category.Coloring));
        });

        view.findViewById(R.id.Main_Transitions).setOnClickListener(v -> {
            EventBus.getDefault().post(new CategorySelectedEvent(Category.Transitions));
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }


    private Long mLastBackPressedTime = null;
    private Toast mExitToast = null;

    @Override
    public boolean onBackPressed() {
        if (mExitToast == null)
            mExitToast = Toast.makeText(getContext(), R.string.exit_toast, Toast.LENGTH_SHORT);

        if (mLastBackPressedTime == null ||
                mLastBackPressedTime + MainViewModel.EXIT_BACK_PRESSES_INTERVAL < System.currentTimeMillis()) {
            mLastBackPressedTime = System.currentTimeMillis();
            mExitToast.show();
            return true;
        }

        mExitToast.cancel();
        return false;
    }
}