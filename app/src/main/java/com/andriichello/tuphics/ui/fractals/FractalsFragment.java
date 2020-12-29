package com.andriichello.tuphics.ui.fractals;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andriichello.tuphics.R;
import com.andriichello.tuphics.fractals.MyAlgorithms;
import com.andriichello.tuphics.interfaces.BackPressedHandler;
import com.andriichello.tuphics.types.Shape;
import com.andriichello.tuphics.ui.adapters.CheckboxAdapter;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.Arrays;

public class FractalsFragment extends Fragment
        implements BackPressedHandler {
    private FractalsViewModel mViewModel;
    private Integer mWhite, mGreen;
    private Handler mHandler;

    private PhotoView mImage;
    private WebView mHelpWebView;

    private View mStatus;
    private RecyclerView mShapesRecycler;
    private CheckboxAdapter mShapesAdapter;
    private int mIterations, mProportion;
    private boolean mIsHelpOpened = false;

    public static FractalsFragment newInstance() {
        return new FractalsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fractals_fragment, container, false);
        mWhite = getResources().getColor(R.color.white, null);
        mGreen = getResources().getColor(R.color.green, null);
        mHandler = new Handler(Looper.getMainLooper());

        mImage = view.findViewById(R.id.Fractals_Image);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FractalsViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();

        mStatus = view.findViewById(R.id.Fractals_Toast);
        mHelpWebView = view.findViewById(R.id.Fractals_Help_Web);

        if (mHelpWebView != null) {
            WebSettings webSettings = mHelpWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setGeolocationEnabled(true);
            webSettings.setLoadsImagesAutomatically(true);
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

            // Enable responsive layout
            webSettings.setUseWideViewPort(true);
            // Zoom out if the content width is greater than the width of the viewport
            webSettings.setLoadWithOverviewMode(true);

            webSettings.setSupportMultipleWindows(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

            mHelpWebView.setWebViewClient(new WebViewClient());
            mHelpWebView.setWebChromeClient(new WebChromeClient());

            mHelpWebView.loadUrl(FractalsViewModel.HELP_URL);
        }

        View toolbar = view.findViewById(R.id.Fractals_Toolbar);
        if (toolbar != null) {
            toolbar.findViewById(R.id.Toolbar_Title).setVisibility(View.VISIBLE);
            ((TextView) toolbar.findViewById(R.id.Toolbar_Title)).setText(R.string.fractals_title);

            View help = view.findViewById(R.id.Fractals_Help);
            View options = view.findViewById(R.id.Fractals_Options);

            ImageView helpIcon = toolbar.findViewById(R.id.Toolbar_RightIcon);
            ImageView optionsIcon = toolbar.findViewById(R.id.Toolbar_LeftIcon);

            helpIcon.setVisibility(View.VISIBLE);
            helpIcon.setImageResource(R.drawable.ic_help);
            helpIcon.setColorFilter(mWhite);

            helpIcon.setOnClickListener(v -> {
                if (help.getVisibility() == View.VISIBLE) {
                    helpIcon.setColorFilter(mWhite);
                    optionsIcon.setVisibility(View.VISIBLE);

                    help.setVisibility(View.GONE);
                    mIsHelpOpened = false;
                } else {
                    helpIcon.setColorFilter(mGreen);
                    optionsIcon.setVisibility(View.GONE);

                    help.setVisibility(View.VISIBLE);
                    mIsHelpOpened = true;
                }
            });


            optionsIcon.setVisibility(View.VISIBLE);
            optionsIcon.setImageResource(R.drawable.ic_options_dark);
            optionsIcon.setColorFilter(mWhite);

            if (options != null) {
                mShapesRecycler = options.findViewById(R.id.Fractal_Options_ShapesRecycler);
                if (mShapesRecycler.getLayoutManager() == null)
                    mShapesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                if (mShapesAdapter == null) {
                    mShapesAdapter = new CheckboxAdapter(getResources(), null);
                    String shape = null;
                    if (mViewModel.getShape().getValue() != null)
                        shape = mViewModel.getShape().getValue().name();
                    mShapesAdapter.setData(Arrays.asList(Shape.Triangle.name(), Shape.Square.name()), shape);
                    mShapesAdapter.setData(Arrays.asList(Shape.Triangle.name(), Shape.Square.name()), shape);
                    mShapesRecycler.setAdapter(mShapesAdapter);
                }

                options.setOnClickListener(v -> { });

                optionsIcon.setOnClickListener(v -> {
                    if (options.getVisibility() != View.VISIBLE) {
                        optionsIcon.setColorFilter(mGreen);
                        options.setVisibility(View.VISIBLE);

                        String shape = null;
                        if (mViewModel.getShape().getValue() != null)
                            shape = mViewModel.getShape().getValue().name();
                        mShapesAdapter.setData(Arrays.asList(Shape.Triangle.name(), Shape.Square.name()), shape);

                        mIterations = mViewModel.getIterations().getValue();
                        View iterations = options.findViewById(R.id.Fractal_Options_Iterations);
                        ((TextView) iterations.findViewById(R.id.Counter_Label)).setText(R.string.iterations);

                        iterations.findViewById(R.id.Counter_Subtract).setEnabled(true);
                        iterations.findViewById(R.id.Counter_Add).setEnabled(true);
                        iterations.findViewById(R.id.Counter_Subtract).setOnClickListener(subtract -> {
                            iterations.findViewById(R.id.Counter_Add).setEnabled(true);

                            --mIterations;
                            if (mIterations <= FractalsViewModel.getIterationsRange().getLower()) {
                                mIterations = FractalsViewModel.getIterationsRange().getLower();
                                iterations.findViewById(R.id.Counter_Subtract).setEnabled(false);
                            }
                            ((TextView) iterations.findViewById(R.id.Counter_Value)).setText(String.valueOf(mIterations));
                        });
                        iterations.findViewById(R.id.Counter_Add).setOnClickListener(add -> {
                            iterations.findViewById(R.id.Counter_Subtract).setEnabled(true);

                            ++mIterations;
                            if (mIterations >= FractalsViewModel.getIterationsRange().getUpper()) {
                                mIterations = FractalsViewModel.getIterationsRange().getUpper();
                                iterations.findViewById(R.id.Counter_Add).setEnabled(false);
                            }
                            ((TextView) iterations.findViewById(R.id.Counter_Value)).setText(String.valueOf(mIterations));
                        });

                        ++mIterations;
                        iterations.findViewById(R.id.Counter_Subtract).callOnClick();

                        mProportion = mViewModel.getProportion().getValue();
                        View proportions = options.findViewById(R.id.Fractal_Options_Proportions);
                        ((TextView) proportions.findViewById(R.id.Counter_Label)).setText(R.string.proportions);

                        proportions.findViewById(R.id.Counter_Subtract).setEnabled(true);
                        proportions.findViewById(R.id.Counter_Add).setEnabled(true);
                        proportions.findViewById(R.id.Counter_Subtract).setOnClickListener(subtract -> {
                            --mProportion;

                            proportions.findViewById(R.id.Counter_Add).setEnabled(true);
                            if (mProportion <= FractalsViewModel.getProportionRange().getLower()) {
                                mProportion = FractalsViewModel.getProportionRange().getLower();
                                proportions.findViewById(R.id.Counter_Subtract).setEnabled(false);
                            }
                            ((TextView) proportions.findViewById(R.id.Counter_Value)).setText(String.format(getString(R.string.N_to_M_format), mProportion, FractalsViewModel.getSplitsCount()));
                        });
                        proportions.findViewById(R.id.Counter_Add).setOnClickListener(add -> {
                            proportions.findViewById(R.id.Counter_Subtract).setEnabled(true);

                            ++mProportion;
                            if (mProportion >= FractalsViewModel.getProportionRange().getUpper()) {
                                mProportion = FractalsViewModel.getProportionRange().getUpper();
                                proportions.findViewById(R.id.Counter_Add).setEnabled(false);
                            }
                            ((TextView) proportions.findViewById(R.id.Counter_Value)).setText(String.format(getString(R.string.N_to_M_format), mProportion, FractalsViewModel.getSplitsCount()));
                        });

                        ++mProportion;
                        proportions.findViewById(R.id.Counter_Subtract).callOnClick();
                    } else {
                        optionsIcon.setColorFilter(mWhite);
                        options.setVisibility(View.GONE);
                    }
                });

                options.findViewById(R.id.Fractal_Options_Apply).setOnClickListener(v -> {
                    mViewModel.setProportion(mProportion);
                    mViewModel.setIterations(mIterations);

                    String shape = mShapesAdapter.getFirst(true);
                    if (shape != null)
                        mViewModel.setShape(Shape.valueOf(shape));

//                    optionsIcon.callOnClick();

//                    Bitmap bitmap = MyAlgorithms.kochFractalBitmap(mIterations, mProportion, mProportion, FractalsViewModel.getSplitsCount(), mViewModel.getShape().getValue(), 2000, 2000);
//                    mImage.setImageBitmap(bitmap);

                    KochFractalCalculationData data = new KochFractalCalculationData(FractalsViewModel.getSplitsCount(), mProportion, mProportion, mIterations , mViewModel.getShape().getValue(), mImage.getWidth(), mImage.getHeight());
//                    KochFractalCalculationData data = new KochFractalCalculationData(FractalsViewModel.getSplitsCount(), mProportion, mProportion, mIterations , mViewModel.getShape().getValue(), 1000, 1000);
                    mStatus.setVisibility(View.VISIBLE);
                    new KochFractalCalculationTask().execute(data);
                });
            }
        }
    }

    public class KochFractalCalculationData {
        private int iterations, a, b, splits;
        private int width, height;
        private Shape shape;

        public KochFractalCalculationData(int splits, int a, int b, int iterations, @NonNull Shape shape, int width, int height) {
            this.iterations = iterations;
            this.a = a;
            this.b = a;
            this.splits = splits;
            this.shape = shape;
            this.width = width;
            this.height = height;
        }

        public int getIterations() {
            return iterations;
        }

        public int getA() {
            return a;
        }

        public int getB() {
            return b;
        }

        public int getSplits() {
            return splits;
        }

        public Shape getShape() {
            return shape;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    private class KochFractalCalculationTask extends AsyncTask<KochFractalCalculationData, Integer, Bitmap> {
        @Override
        protected Bitmap doInBackground(KochFractalCalculationData... values) {
            KochFractalCalculationData data = values[0];
            if (data == null)
                return null;

            return MyAlgorithms.kochFractalBitmap(data.iterations, data.a, data.b, data.splits, data.shape, data.width, data.height);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            mHandler.post(() -> {
                mStatus.setVisibility(View.GONE);
                float scale = mImage.getScale();

                mImage.setImageBitmap(bitmap);
                mImage.setScale(scale);
            });
        }
    }

    @Override
    public boolean onBackPressed() {
        if (mIsHelpOpened) {
            getView().findViewById(R.id.Toolbar_RightIcon).callOnClick();
            return true;
        }
        return false;
    }
}
