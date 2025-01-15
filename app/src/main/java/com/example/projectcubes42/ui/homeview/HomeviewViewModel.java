package com.example.projectcubes42.ui.homeview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeviewViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeviewViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}