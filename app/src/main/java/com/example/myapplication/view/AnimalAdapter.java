package com.example.myapplication.view;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.AnimalRepository;
import com.example.myapplication.model.Animal;

import java.util.List;

/**
 * Adapter for displaying a list of animals in a RecyclerView.
 */
public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder> {

    private List<Animal> animals;
    private final AnimalRepository repository;

    /**
     * Constructor for the AnimalAdapter.
     *
     * @param repository The data repository for animal data operations.
     */
    public AnimalAdapter(AnimalRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.animal_layout, parent, false);
        return new AnimalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalViewHolder holder, int position) {
        if (animals == null || animals.isEmpty()) {
            return; // Early return if there is no data.
        }

        Animal animal = animals.get(position);
        holder.textView.setText(animal.getName());
        holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(animal.getImage(), 0, animal.getImage().length));
        holder.markedForDelete.setChecked(animal.isMarkedForDelete());

        holder.markedForDelete.setOnClickListener(view -> {
            animal.setMarkedForDelete(true);
            notifyItemChanged(position); // Notify that item has changed to refresh the view.
        });
    }

    @Override
    public int getItemCount() {
        return animals != null ? animals.size() : 0;
    }

    /**
     * Updates the list of animals this adapter handles.
     *
     * @param animals The new list of animals.
     */
    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
        notifyDataSetChanged(); // Notify that data set has changed to refresh the entire list.
    }

    /**
     * Initiates the deletion of marked animals from the dataset and database.
     */
    @SuppressLint("NotifyDataSetChanged")
    public void deleteMarkedAnimals() {
        new Thread(() -> {
            animals.removeIf(animal -> {
                if (animal.isMarkedForDelete()) {
                    repository.deleteAnimal(animal);
                    return true;
                }
                return false;
            });
            notifyDataSetChanged(); // Notify from the UI thread to avoid concurrency issues.
        }).start();
    }
    public List<Animal> getAnimals() {
        return animals;
    }
    /**
     * ViewHolder for animal items in the list.
     */
    static class AnimalViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;
        CheckBox markedForDelete;

        public AnimalViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
            imageView = itemView.findViewById(R.id.image_view);
            markedForDelete = itemView.findViewById(R.id.markedfordelete);
        }
    }
}
