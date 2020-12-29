package com.andriichello.tuphics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.andriichello.tuphics.coloring.HSVColor;
import com.andriichello.tuphics.coloring.RGBColor;
import com.andriichello.tuphics.events.CategorySelectedEvent;
import com.andriichello.tuphics.fractals.MyAlgorithms;
import com.andriichello.tuphics.interfaces.BackPressedHandler;
import com.andriichello.tuphics.types.Category;
import com.andriichello.tuphics.types.Line;
import com.andriichello.tuphics.types.Polygon;
import com.andriichello.tuphics.types.Segment;
import com.andriichello.tuphics.types.Shape;
import com.andriichello.tuphics.types.TransitionMatrix;
import com.andriichello.tuphics.ui.coloring.ImageColoringFragment;
import com.andriichello.tuphics.ui.fractals.FractalsFragment;
import com.andriichello.tuphics.ui.main.MainFragment;
import com.andriichello.tuphics.ui.transitions.TransitionsFragment;
import com.andriichello.tuphics.ui.transitions.TransitionsViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Arrays;
import java.util.List;

// Варіант І2.
//  DONE: 1) Фрактал універсальна замкнена лінія Коха, для якої вводиться базова фігура (трикутник, квадрат)
//   і відношення, що задаватиме розбиття сторони фігури.

//  DONE: 2) Кольорні моделі: RGB і HSV. Змінити насиченість по зеленому кольору.

//  TODO: 3) Реалізувати рух для паралелограма, введеного за його вершинами,
//   на основі повороту за годинниковою стрілкою відносно вибраної вершини з одночасним його зменшення у А разів.

public class MainActivity extends AppCompatActivity {
    private static final String MAIN_TAG = "MAIN";

    FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mFragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            mFragmentManager.beginTransaction()
                    .add(R.id.container, MainFragment.newInstance(), MAIN_TAG)
                    .addToBackStack(MAIN_TAG)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (fragments.size() > 0) {
            Fragment fragment = fragments.get(fragments.size() - 1);
            if (fragment instanceof BackPressedHandler) {
                boolean result = ((BackPressedHandler) fragment).onBackPressed();
                if (!result) {
                    if (fragment instanceof MainFragment)
                        moveTaskToBack(true);
                    else
                        mFragmentManager.popBackStack();
                }

                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onCategorySelectedEvent(CategorySelectedEvent event) {
        if (event == null || event.getCategory() == null)
            return;

        switch (event.getCategory()) {
            case Fractals:
                mFragmentManager.beginTransaction()
                        .add(R.id.container, FractalsFragment.newInstance(), Category.Fractals.name())
                        .addToBackStack(Category.Fractals.name())
                        .commit();
                break;

            case Coloring:
                mFragmentManager.beginTransaction()
                        .add(R.id.container, ImageColoringFragment.newInstance(), Category.Coloring.name())
                        .addToBackStack(Category.Coloring.name())
                        .commit();
                break;

            case Transitions:
                mFragmentManager.beginTransaction()
                        .add(R.id.container, TransitionsFragment.newInstance(), Category.Transitions.name())
                        .addToBackStack(Category.Transitions.name())
                        .commit();
                break;
        }



    }
}