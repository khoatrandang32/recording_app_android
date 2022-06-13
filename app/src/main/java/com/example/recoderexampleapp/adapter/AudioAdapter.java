package com.example.recoderexampleapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recoderexampleapp.R;
import com.example.recoderexampleapp.models.MyAudio;
import com.example.recoderexampleapp.utils.Utils;

import java.util.ArrayList;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioAdapterViewHolder> {

    ArrayList<MyAudio> listAudio;
    AudioAdapterInterface listener;

    public AudioAdapter(ArrayList<MyAudio> listAudio, AudioAdapterInterface listener) {
        this.listAudio = listAudio;
        this.listener = listener;
    }

    public interface AudioAdapterInterface{
        public void onItemClicked(MyAudio item);
    }

    @NonNull
    @Override
    public AudioAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AudioAdapterViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.audio_item_layout, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AudioAdapterViewHolder holder, int position) {
        MyAudio item = listAudio.get(position);
        holder.txtTitle.setText(item.getName());
        holder.txtDesc.setText(Utils.timeToString(item.getDuration())+"   "+Utils.getDate(item.getCreateAt(),"dd/MM/yyyy"));
        holder.layoutContainer.setOnClickListener(view -> {
            listener.onItemClicked(item);
        });
    }

    @Override
    public int getItemCount() {
        return listAudio.size();
    }

    class AudioAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle,txtDesc;
        LinearLayout layoutContainer;

        public AudioAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDesc = itemView.findViewById(R.id.txtDesc);
            layoutContainer = itemView.findViewById(R.id.layoutContainer);
            txtTitle = itemView.findViewById(R.id.txtTitle);

        }

    }
}
