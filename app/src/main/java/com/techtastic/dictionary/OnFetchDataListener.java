package com.techtastic.dictionary;

import com.techtastic.dictionary.Models.APIResponse;

public interface OnFetchDataListener {

    void onFetchData(APIResponse apiResponse, String message);
    void onError(String message);
}
