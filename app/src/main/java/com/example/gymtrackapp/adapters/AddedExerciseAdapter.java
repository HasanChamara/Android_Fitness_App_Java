package com.example.gymtrackapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymtrackapp.R;

import java.util.List;
public class AddedExerciseAdapter extends RecyclerView.Adapter<AddedExerciseAdapter.ViewHolder> {
    private static List<List<String>> exerciseList;

    public AddedExerciseAdapter(List<List<String>> exerciseList) {
        this.exerciseList = exerciseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_added_exercise, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<String> exercise = exerciseList.get(position);
        holder.exerciseNameTextView.setText(exercise.get(0));
        holder.setsTextView.setText(exercise.get(1));
        holder.repsTextView.setText( exercise.get(2));
        holder.dayTextView.setText( exercise.get(3));
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView exerciseNameTextView;
        TextView setsTextView;
        TextView repsTextView;
        TextView dayTextView;
        Button closeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseNameTextView = itemView.findViewById(R.id.exerciseNameTextView);
            setsTextView = itemView.findViewById(R.id.setsTextView);
            repsTextView = itemView.findViewById(R.id.repsTextView);
            dayTextView = itemView.findViewById(R.id.dayTextView);
            closeButton = itemView.findViewById(R.id.closeButton);

            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        exerciseList.remove(position);
                        notifyItemRemoved(position);
                    }
                }
            });
        }
    }
}
