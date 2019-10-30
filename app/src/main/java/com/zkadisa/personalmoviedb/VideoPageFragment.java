package com.zkadisa.personalmoviedb;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class VideoPageFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_TITLE = "title";
    private static final String ARG_URL = "url";

    private VideoPageViewModel pageViewModel;

    public static VideoPageFragment newInstance(int index, String title, String url) {
        VideoPageFragment fragment = new VideoPageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        bundle.putString(ARG_TITLE, title);
        bundle.putString(ARG_URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(VideoPageViewModel.class);
        int index = 1;
        String title;
        String url;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
            title = getArguments().getString(ARG_TITLE);
            url = getArguments().getString(ARG_URL);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        String url = getArguments().getString(ARG_URL);
        View root = inflater.inflate(R.layout.videofragment, container, false);
        WebView webView = root.findViewById(R.id.WebView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient());

        return root;
    }
}