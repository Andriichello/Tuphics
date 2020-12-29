package com.andriichello.tuphics.ui.coloring;

import androidx.lifecycle.ViewModelProvider;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
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
import android.widget.Toast;

import com.andriichello.tuphics.R;
import com.andriichello.tuphics.coloring.HSVColor;
import com.andriichello.tuphics.coloring.ImageUtils;
import com.andriichello.tuphics.coloring.MyAlgorithms;
import com.andriichello.tuphics.interfaces.BackPressedHandler;
import com.andriichello.tuphics.ui.fractals.FractalsViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImageColoringFragment extends Fragment
        implements BackPressedHandler {
    private static final int PICK_IMAGE = 100;

    private ImageView mInputImage, mOutputImage;
    private View mToast;
    private TextView mRGBLabel;
    private WebView mHelpWebView;

    private Integer mWhite, mGreen;
    private boolean mIsHelpOpened;

    private ImageColoringViewModel mViewModel;
    private Handler mHandler = new Handler(Looper.getMainLooper());


    public static ImageColoringFragment newInstance() {
        return new ImageColoringFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.image_coloring_fragment, container, false);

        mWhite = getResources().getColor(R.color.white, null);
        mGreen = getResources().getColor(R.color.green, null);

        mHelpWebView = view.findViewById(R.id.ImageColoring_Help_Web);

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

            mHelpWebView.loadUrl(ImageColoringViewModel.HELP_URL);
        }

        mRGBLabel = view.findViewById(R.id.Coloring_Options_RGB);
        mToast = view.findViewById(R.id.ImageColoring_Toast);

        View toolbar = view.findViewById(R.id.ImageColoring_Toolbar);

        ((TextView) toolbar.findViewById(R.id.Toolbar_Title)).setText(R.string.imagecoloring_title);

        View help = view.findViewById(R.id.ImageColoring_Help);
        View options = view.findViewById(R.id.ImageColoring_Options);

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
            options.setOnClickListener(v -> {
            });

            optionsIcon.setOnClickListener(v -> {
                if (options.getVisibility() != View.VISIBLE) {
                    optionsIcon.setColorFilter(mGreen);
                    options.setVisibility(View.VISIBLE);
                } else {
                    optionsIcon.setColorFilter(mWhite);
                    options.setVisibility(View.GONE);
                }
            });

            SeekBar hSeekBar = options.findViewById(R.id.Coloring_Options_Hue_Slider);
            hSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    mViewModel.setHue((double) progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            SeekBar sSeekBar = options.findViewById(R.id.Coloring_Options_Saturation_Slider);
            sSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    mViewModel.setSaturation((double) progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            SeekBar vSeekBar = options.findViewById(R.id.Coloring_Options_Value_Slider);
            vSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    mViewModel.setValue((double) progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            options.findViewById(R.id.Coloring_Options_Apply).setOnClickListener(v -> {
                HSVColor hsv = new HSVColor(mViewModel.getHue().getValue(), mViewModel.getSaturation().getValue(), mViewModel.getValue().getValue());
                Uri imageUri = mViewModel.getImageUri().getValue();
                if (imageUri != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                        mViewModel.setInputBitmap(bitmap);
                        if (bitmap != null) {
                            ColoringThread coloringThread = new ColoringThread(hsv);
                            coloringThread.start();

                            mToast.setVisibility(View.VISIBLE);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        mInputImage = view.findViewById(R.id.ImageColoring_Input_Image);
        mOutputImage = view.findViewById(R.id.ImageColoring_Output_Image);

        view.findViewById(R.id.ImageColoring_Input_Button).setOnClickListener(v -> {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, PICK_IMAGE);
        });

        view.findViewById(R.id.ImageColoring_Output_Button).setOnClickListener(v -> {
            Bitmap bm = mViewModel.getOutputBitmap().getValue();
            if (bm != null) {
                boolean wasSaved = ImageUtils.saveImageToGallery(getContext(), bm);
                if (wasSaved)
                    Toast.makeText(getContext(), "Picture saved", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "Error. While saving picture", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mViewModel.getHue().observe(getViewLifecycleOwner(), aDouble -> {
            ((TextView) getView().findViewById(R.id.Coloring_Options_Hue_Number)).setText(String.valueOf(aDouble));
            updateRGBColorLabel();
        });
        mViewModel.getSaturation().observe(getViewLifecycleOwner(), aDouble -> {
            ((TextView) getView().findViewById(R.id.Coloring_Options_Saturation_Number)).setText(String.valueOf(aDouble));
            updateRGBColorLabel();
        });
        mViewModel.getValue().observe(getViewLifecycleOwner(), aDouble -> {
            ((TextView) getView().findViewById(R.id.Coloring_Options_Value_Number)).setText(String.valueOf(aDouble));
            updateRGBColorLabel();
        });
        mViewModel.getImageUri().observe(getViewLifecycleOwner(), imageUri -> {
            if (imageUri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    mViewModel.setInputBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mViewModel.getInputBitmap().observe(getViewLifecycleOwner(), bitmap -> {
            mInputImage.setImageBitmap(bitmap);
        });

        mViewModel.getOutputBitmap().observe(getViewLifecycleOwner(), bitmap -> {
            mOutputImage.setImageBitmap(bitmap);
            mToast.setVisibility(View.GONE);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ImageColoringViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == PICK_IMAGE) {
            Uri uri = data.getData();
            mViewModel.setImageUri(uri);
        }

    }

    public class ColoringThread extends Thread {
        private HSVColor hsv;

        public ColoringThread(HSVColor hsv) {
            this.hsv = hsv;
        }

        public void run() {
            Bitmap bm = MyAlgorithms.applyHSV(mViewModel.getInputBitmap().getValue(), hsv, mViewModel.getSensitivity().getValue());
            if (mHandler != null) {
                if (mHandler != null)
                mHandler.post(() -> {
                    mViewModel.setOutputBitmap(bm);
                });
            }

        }
    }

    public void updateRGBColorLabel() {
        mRGBLabel.setText(mViewModel.getHSVColor().toRGB().toString());
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