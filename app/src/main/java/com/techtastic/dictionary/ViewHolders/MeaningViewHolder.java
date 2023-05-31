package com.techtastic.dictionary.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techtastic.dictionary.R;

public class MeaningViewHolder extends RecyclerView.ViewHolder {

    public TextView textView_partsOfSpeech;
    public RecyclerView recycler_definations;

    public MeaningViewHolder(@NonNull View itemView) {
        super(itemView);

        textView_partsOfSpeech = itemView.findViewById(R.id.textView_partsOfSpeech);
        recycler_definations = itemView.findViewById(R.id.recycler_definations);
    }
}
