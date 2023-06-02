package com.techtastic.dictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.techtastic.dictionary.Adapters.MeaningAdapter;
import com.techtastic.dictionary.Adapters.PhoneticsAdapter;
import com.techtastic.dictionary.Models.APIResponse;
import com.techtastic.dictionary.Models.Definations;
import com.techtastic.dictionary.Models.Meanings;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    SearchView search_view;
    TextView textView_word, textView_origin, textView_phonetic;
    RecyclerView recycler_phonetics, recycler_meanings;
    ProgressDialog progressDialog;
    PhoneticsAdapter phoneticsAdapter;
    MeaningAdapter meaningAdapter;
    ImageButton imageButton_share,imageButton_copy;
    List<Meanings> meanlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        search_view = findViewById(R.id.search_view);
        textView_word = findViewById(R.id.textView_word);
        textView_phonetic = findViewById(R.id.textView_phonetic);
        textView_origin = findViewById(R.id.textView_origin);
        recycler_phonetics = findViewById(R.id.recycler_phonetics);
        recycler_meanings = findViewById(R.id.recycler_meanings);
        imageButton_share = findViewById(R.id.imageButton_share);
        imageButton_copy = findViewById(R.id.imageButton_copy);
        progressDialog = new ProgressDialog(this);


        progressDialog.setTitle("Loading......");
        progressDialog.show();
        RequestManager manager = new RequestManager(MainActivity.this);
        manager.getWordMeaning(Listener, "hello");


        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressDialog.setTitle("Fetching response for " + query);
                progressDialog.show();
                RequestManager manager = new RequestManager(MainActivity.this);
                manager.getWordMeaning(Listener, query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private final OnFetchDataListener Listener = new OnFetchDataListener() {
        @Override
        public void onFetchData(APIResponse apiResponse, String message) {
            progressDialog.dismiss();
            if (apiResponse == null) {
                Toast.makeText(MainActivity.this, "No Data Found!!!", Toast.LENGTH_SHORT).show();
                return;
            }

            showData(apiResponse);
        }

        @Override
        public void onError(String message) {
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

        }
    };

    private void showData(APIResponse apiResponse) {
        textView_word.setText(apiResponse.getWord());

        textView_phonetic.setText("[ " + apiResponse.getPhonetic() + " ]");

        recycler_phonetics.setHasFixedSize(true);
        recycler_phonetics.setLayoutManager(new GridLayoutManager(this, 3));
        phoneticsAdapter = new PhoneticsAdapter(this, apiResponse.getPhonetics());
        recycler_phonetics.setAdapter(phoneticsAdapter);

        textView_origin.setText("Origin: " + apiResponse.getOrigin());

        recycler_meanings.setHasFixedSize(true);
        recycler_meanings.setLayoutManager(new GridLayoutManager(this, 1));
        meaningAdapter = new MeaningAdapter(this, apiResponse.getMeanings());
        recycler_meanings.setAdapter(meaningAdapter);



        if (apiResponse.getMeanings() != null && !apiResponse.getMeanings().isEmpty()) {
            imageButton_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<Meanings> meanings = apiResponse.getMeanings();
                    if (meanings.size() >= 2) {
                        List<Definations> definitions = meanings.get(1).getDefinitions();
                        if (definitions != null && !definitions.isEmpty()) {
                            Definations firstDefinition = definitions.get(0);
                            String definition = firstDefinition.getDefinition();

                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT, "Word: " + apiResponse.getWord() + "\nMeaning: " + definition);
                            intent.setType("text/plain");

                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivity(intent);
                            }
                        }
                    }
                }
            });
        }

        if (apiResponse.getMeanings() != null && !apiResponse.getMeanings().isEmpty()) {
            imageButton_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<Meanings> meanings = apiResponse.getMeanings();
                    if (meanings.size() >= 2) {
                        List<Definations> definitions = meanings.get(0).getDefinitions();
                        if (definitions != null && !definitions.isEmpty()) {
                            Definations firstDefinition = definitions.get(0);
                            String definition = firstDefinition.getDefinition();

                            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clipData = ClipData.newPlainText("copy", apiResponse.getWord() + "\nMeaning: "+definition);
                            clipboardManager.setPrimaryClip(clipData);
                            Toast.makeText(MainActivity.this, "Copied", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }




    }
}