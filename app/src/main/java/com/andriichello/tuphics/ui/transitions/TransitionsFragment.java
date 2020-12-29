package com.andriichello.tuphics.ui.transitions;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.SeekBar;
import android.widget.TextView;

import com.andriichello.tuphics.R;
import com.andriichello.tuphics.graphview.Graph;
import com.andriichello.tuphics.graphview.GraphView;
import com.andriichello.tuphics.graphview.Point;
import com.andriichello.tuphics.interfaces.BackPressedHandler;
import com.andriichello.tuphics.types.Pair;
import com.andriichello.tuphics.types.Polygon;
import com.andriichello.tuphics.types.TransitionMatrix;
import com.andriichello.tuphics.ui.coloring.ImageColoringViewModel;
import com.andriichello.tuphics.ui.transitions.AnchorsAdapter;
import com.andriichello.tuphics.ui.transitions.TransitionsViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransitionsFragment extends Fragment
        implements BackPressedHandler {

    private TransitionsViewModel mViewModel;
    private Integer mWhite, mGreen;
    private boolean mIsHelpOpened;

    private ImageView mView;
    private GraphView mGraphView;
    private AnchorsAdapter mAnchorsAdapter, mPointsAdapter;
    private WebView mHelpWebView;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public static TransitionsFragment newInstance() {
        return new TransitionsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transitions_fragment, container, false);

        mWhite = getResources().getColor(R.color.white, null);
        mGreen = getResources().getColor(R.color.green, null);

        mHelpWebView = view.findViewById(R.id.Transitions_Help_Web);
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


            mHelpWebView.loadUrl(TransitionsViewModel.HELP_URL);
        }

        mView = view.findViewById(R.id.Transitions_View);
        mGraphView = (GraphView) view.findViewById(R.id.Transitions_GraphView);
        mGraphView.setBackgroundColor(mWhite);

        View toolbar = view.findViewById(R.id.Transitions_Toolbar);
        ((TextView) toolbar.findViewById(R.id.Toolbar_Title)).setText(R.string.transitions_title);

        View help = view.findViewById(R.id.Transitions_Help);
        View options = view.findViewById(R.id.Transitions_Options);

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

        // setting up options icon click listener
        optionsIcon.setVisibility(View.VISIBLE);
        optionsIcon.setImageResource(R.drawable.ic_options_light);
        optionsIcon.setOnClickListener(v -> {
            if (options.getVisibility() != View.VISIBLE) {
                options.setVisibility(View.VISIBLE);
                optionsIcon.setColorFilter(mGreen);
            } else {
                options.setVisibility(View.GONE);
                optionsIcon.setColorFilter(mWhite);
            }
        });

        // setting up anchors recycler
        RecyclerView anchorsRecycler = options.findViewById(R.id.Transition_Options_Anchors_Recycler);
        anchorsRecycler.setLayoutManager(new GridLayoutManager(getContext(), 1, RecyclerView.HORIZONTAL, false));
        mAnchorsAdapter = new AnchorsAdapter(true);
        anchorsRecycler.setAdapter(mAnchorsAdapter);

        // setting up result points recycler
        RecyclerView pointsRecycler = options.findViewById(R.id.Transition_Options_Points_Recycler);
        anchorsRecycler.setLayoutManager(new GridLayoutManager(getContext(), 1, RecyclerView.HORIZONTAL, false));
        mPointsAdapter = new AnchorsAdapter(false);
        pointsRecycler.setAdapter(mPointsAdapter);

        // setting up SeekBars
        SeekBar anchorBar = options.findViewById(R.id.Transition_Options_Anchor_Slider);
        SeekBar scaleBar = options.findViewById(R.id.Transition_Options_Scale_Slider);
        SeekBar rotationBar = options.findViewById(R.id.Transition_Options_Rotation_Slider);

        // setting up SeekBars ranges
        anchorBar.setMax(TransitionsViewModel.ANCHORS_RANGE.getUpper() - TransitionsViewModel.ANCHORS_RANGE.getLower());
        scaleBar.setMax(TransitionsViewModel.SCALE_RANGE.getUpper());
        rotationBar.setMax(TransitionsViewModel.ROTATION_RANGE.getUpper());

        // setting up SeekBars listeners
        anchorBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mViewModel.setAnchor((char) (TransitionsViewModel.ANCHORS_RANGE.getLower() + progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        scaleBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mViewModel.setScale(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        rotationBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mViewModel.setRotation(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // setting up apply button's click listener
        options.findViewById(R.id.Transition_Options_Apply).setOnClickListener(v -> {
            mViewModel.setAnchors(mAnchorsAdapter.getPairs());
            mViewModel.setAnchor((char) (anchorBar.getProgress() + TransitionsViewModel.ANCHORS_RANGE.getLower()));
            mViewModel.setScale(scaleBar.getProgress());
            mViewModel.setRotation(rotationBar.getProgress());

            Polygon polygon = new Polygon();
            for (Pair<String, Pair<Float, Float>> pair : mViewModel.getAnchors().getValue()) {
                if (pair.second == null) {
                    return;
                }

                polygon.points.add(new PointF(pair.second.first, pair.second.second));
            }

            float width, height;
            width = mView.getWidth();
            height = mView.getHeight();


            TransitionCalculationTread calculationTread = new TransitionCalculationTread(polygon, width, height);
            calculationTread.start();
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mViewModel.setAnchors(Arrays.asList(
                new Pair<String, Pair<Float, Float>>("A", new Pair<Float, Float>(0f, 0f)),
                new Pair<String, Pair<Float, Float>>("B", new Pair<Float, Float>(0f, 50f)),
                new Pair<String, Pair<Float, Float>>("C", new Pair<Float, Float>(40f, 80f)),
                new Pair<String, Pair<Float, Float>>("D", new Pair<Float, Float>(40f, 30f))
        ));

        // setting up SeekBars
        SeekBar anchorBar = getView().findViewById(R.id.Transition_Options_Anchor_Slider);
        SeekBar scaleBar = getView().findViewById(R.id.Transition_Options_Scale_Slider);
        SeekBar rotationBar = getView().findViewById(R.id.Transition_Options_Rotation_Slider);

        anchorBar.setProgress(mViewModel.getAnchor().getValue() % TransitionsViewModel.ANCHORS_RANGE.getLower());
        ((TextView) getView().findViewById(R.id.Transition_Options_Anchor_Number)).setText(String.valueOf(mViewModel.getAnchor().getValue()));
        scaleBar.setProgress(mViewModel.getScale().getValue());
        ((TextView) getView().findViewById(R.id.Transition_Options_Scale_Number)).setText(String.valueOf(mViewModel.getScale().getValue()));
        rotationBar.setProgress(mViewModel.getRotation().getValue());
        ((TextView) getView().findViewById(R.id.Transition_Options_Rotation_Number)).setText(String.valueOf(mViewModel.getRotation().getValue()));

        // setting up anchors adapter
        if (mAnchorsAdapter != null)
            mAnchorsAdapter.setPairs(mViewModel.getAnchors().getValue());

        // setting up result points adapter
        if (mPointsAdapter != null)
            mPointsAdapter.setAnchors(null);

        // setting up observers for ViewModel
        mViewModel.getAnchor().observe(getViewLifecycleOwner(), anchor -> {
            ((TextView) getView().findViewById(R.id.Transition_Options_Anchor_Number)).setText(String.valueOf(anchor));
        });
        mViewModel.getScale().observe(getViewLifecycleOwner(), scale -> {
            String text = String.valueOf(scale) + " to " + String.valueOf(TransitionsViewModel.SCALE_MIDDLE_VALUE);
            ((TextView) getView().findViewById(R.id.Transition_Options_Scale_Number)).setText(text);
        });
        mViewModel.getRotation().observe(getViewLifecycleOwner(), rotation -> {
            ((TextView) getView().findViewById(R.id.Transition_Options_Rotation_Number)).setText(String.valueOf(rotation));
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TransitionsViewModel.class);
    }


    public class TransitionCalculationTread extends Thread {
        public int FRAMES_COUNT = 1;
        public int STEPS_COUNT = 5;

        private Polygon polygon;
        float width, height;

        Float minX, minY, maxX, maxY;
        List<Point> points;

        public TransitionCalculationTread(Polygon polygon, float width, float height) {
            this.polygon = polygon;

            this.width = width;
            this.height = height;
        }

        @Override
        public void run() {
            List<Polygon> polygons = new ArrayList<>();

            double degrees = mViewModel.getRotation().getValue();
            PointF origin = mViewModel.getAnchorPointF();

            float distance = Math.max(width, height);
            float step = (float) (distance / (STEPS_COUNT));

            Integer scale = mViewModel.getScale().getValue();
            if (scale == null)
                scale = TransitionsViewModel.SCALE_MIDDLE_VALUE;

            double xFactor = (double) scale / TransitionsViewModel.SCALE_MIDDLE_VALUE;
            double yFactor = (double) scale / TransitionsViewModel.SCALE_MIDDLE_VALUE;
            Polygon rotated = polygon.scale(xFactor, yFactor);

            float side = (float) rotated.maxSideLength();

            minX = minY = maxX = maxY = null;
            for (PointF pointF : rotated.points) {
                if (maxX == null)
                    maxX = pointF.x;
                if (minX == null)
                    minX = pointF.x;

                if (maxY == null)
                    maxY = pointF.y;
                if (minY == null)
                    minY = pointF.y;

                if (maxX < pointF.x)
                    maxX = pointF.x;
                if (minX > pointF.x)
                    minX = pointF.x;
                if (maxY < pointF.y)
                    maxY = pointF.y;
                if (minY > pointF.y)
                    minY = pointF.y;
            }

            maxX += side * 2f;
            minX -= side * 2f;
            maxY += side * 2f;
            minY -= side * 2f;

            List<Double> xTicks, yTicks;
            float xStep, yStep;
            xStep = (maxX - minX) / (STEPS_COUNT);
            yStep = (maxY - minY) / (STEPS_COUNT);

            xTicks = new ArrayList<>();
            double x = minX;
            while (xStep > 0 && x <= maxX) {
                xTicks.add((double) ((int) (x * 100) / 100));
                x += xStep;
            }

            yTicks = new ArrayList<>();
            double y = minY;
            while (yStep > 0 && y <= maxY) {
                yTicks.add((double) ((int) (y * 100) / 100));
                y += yStep;
            }

            double[][] allPoints = new double[polygon.points.size()][3];
            for (int i = 0; i < allPoints.length; i++) {
                allPoints[i][0] = polygon.points.get(i).x;
                allPoints[i][1] = polygon.points.get(i).y;
                allPoints[i][2] = 1.0;
            }

            TransitionMatrix m;
            m = TransitionMatrix.Builder.transition(-origin.x, -origin.y);
            for (int i = 0; i < allPoints.length; i++)
                allPoints[i] = m.toCoordinates(allPoints[i]);

            m = TransitionMatrix.Builder.dilation((double)scale / TransitionsViewModel.SCALE_MIDDLE_VALUE, (double)scale / TransitionsViewModel.SCALE_MIDDLE_VALUE);
            for (int i = 0; i < allPoints.length; i++)
                allPoints[i] = m.toCoordinates(allPoints[i]);

            m = TransitionMatrix.Builder.rotation(Math.toRadians(degrees));
            for (int i = 0; i < allPoints.length; i++)
                allPoints[i] = m.toCoordinates(allPoints[i]);

            m = TransitionMatrix.Builder.transition(origin.x, origin.y);
            for (int i = 0; i < allPoints.length; i++)
                allPoints[i] = m.toCoordinates(allPoints[i]);

            Polygon transformed = new Polygon(polygon);
            for (int i = 0; i < transformed.points.size(); i++)
                transformed.points.set(i, new PointF((float) allPoints[i][0], (float) allPoints[i][1]));

            points = new ArrayList<>();
            for (PointF p : transformed.points)
                points.add(new Point(p.x, p.y));
            points.add(points.get(0));


            Polygon finalTransformed = transformed;
            mHandler.post(() -> {
                List<Pair<String, Pair<Float, Float>>> pairs = new ArrayList<>();
                if (mViewModel.getAnchors().getValue() != null) {
                    int index = 0;
                    for (Pair<String, Pair<Float, Float>> pair : mViewModel.getAnchors().getValue()) {
                        if (index >= finalTransformed.points.size())
                            break;

                        pairs.add(new Pair<>(pair.first, new Pair<>((int) (finalTransformed.points.get(index).x * 10000) / 10000f, (int) (finalTransformed.points.get(index).y * 10000) / 10000f)));
                        index++;
                    }
                }

                mPointsAdapter.setPairs(pairs);

                Graph graph = new Graph.Builder()
                        .addLineGraph(points, Color.RED)
                        .setWorldCoordinates(minX, maxX, minY, maxY)
                        .setXTicks(xTicks)
                        .setYTicks(yTicks)
                        .build();

                mGraphView.setGraph(graph);

            });

//            rotated = rotated.rotate(degrees, origin);
//            points = new ArrayList<>();
//            for (PointF p : rotated.points)
//                points.add(new Point(p.x, p.y));
//            points.add(points.get(0));
//
//
//            Polygon finalRotated = rotated;
//            mHandler.post(() -> {
//                List<Pair<String, Pair<Float, Float>>> pairs = new ArrayList<>();
//                if (mViewModel.getAnchors().getValue() != null) {
//                    int index = 0;
//                    for (Pair<String, Pair<Float, Float>> pair : mViewModel.getAnchors().getValue()) {
//                        if (index >= finalRotated.points.size())
//                            break;
//
//                        pairs.add(new Pair<>(pair.first, new Pair<>((int) (finalRotated.points.get(index).x * 10000) / 10000f, (int) (finalRotated.points.get(index).y * 10000) / 10000f)));
//                        index++;
//                    }
//                }
//
//                mPointsAdapter.setPairs(pairs);
//
//                Graph graph = new Graph.Builder()
//                        .addLineGraph(points, Color.RED)
//                        .setWorldCoordinates(minX, maxX, minY, maxY)
//                        .setXTicks(xTicks)
//                        .setYTicks(yTicks)
//                        .build();
//
//                mGraphView.setGraph(graph);
//
//            });


//            double step = degrees / FRAMES_COUNT;
//            for (double a = step; a <= degrees; a++)
//                polygons.add(polygon.rotate(a, origin));
//
//            mHandler.post(() -> {
//                List<Point> points = new ArrayList<>();
//                for (PointF p : polygons.get(0).points)
//                    points.add(new Point(p.x, p.y));
//
//                if (points.size() > 0) {
//                    points.add(points.get(0));
//
//                    minX = minY = maxX = maxY = null;
//
//                    for (int i = 0; i < polygons.size(); i++) {
//                        Polygon polygon = polygons.get(i);
//                        if (polygon == null || polygon.points == null || polygon.points.isEmpty())
//                            continue;
//
//                        if (minX == null) minX = polygon.points.get(0).x;
//                        if (maxX == null) maxX = polygon.points.get(0).x;
//                        if (minY == null) minY = polygon.points.get(0).y;
//                        if (maxY == null) maxY = polygon.points.get(0).y;
//
//                        for (PointF point : polygon.points) {
//                            if (minX > point.x) minX = point.x;
//                            if (maxX < point.x) maxX = point.x;
//                            if (minY > point.y) minY = point.y;
//                            if (maxY < point.y) maxY = point.y;
//                        }
//                    }
//
//                    List<Double> xTicks, yTicks;
//                    minX -= (maxX - minX) * 2;
//                    minY -= (maxY - minY) * 2;
//                    maxX += (maxX - minX) * 2;
//                    maxY += (maxY - minY) * 2;
//
//                    float xStep, yStep;
//                    xStep = (maxX - minX) / (STEPS_COUNT + 2);
//                    yStep = (maxY - minY) / (STEPS_COUNT + 2);
//
//                    xTicks = new ArrayList<>();
//                    double x = minX;
//                    while (x <= maxX) {
//                        xTicks.add((double) ((int) (x * 100) / 100));
//                        x += xStep;
//                    }
//
//                    yTicks = new ArrayList<>();
//                    double y = minY;
//                    while (y <= maxY) {
//                        yTicks.add((double) ((int) (y * 100) / 100));
//                        y += yStep;
//                    }
//
//                    Graph graph = new Graph.Builder()
//                            .addLineGraph(points, Color.RED)
//                            .setWorldCoordinates(minX, maxX, minY, maxY)
//                            .setXTicks(xTicks)
//                            .setYTicks(yTicks)
//                            .build();
//
//                    mGraphView.setGraph(graph);
//                }
//            });
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