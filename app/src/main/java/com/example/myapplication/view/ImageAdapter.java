package com.example.myapplication.view;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.example.myapplication.R;
import com.example.myapplication.model.Image;
import com.example.myapplication.database.ImageRepository;
import com.example.myapplication.util.DatabaseUtil;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    List<Image> items;
    ImageRepository repository;

    public ImageAdapter(ImageRepository repo) {
        this.repository = repo;
        this.items = repo.getAllImages().getValue();
        if (this.items == null) this.items = new ArrayList<>();
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
        final Image image = items.get(position);

        holder.imageView.setImageBitmap(DatabaseUtil.getBitmapFromStorage(image.getPath()));
        holder.textView.setText(image.getName());
        holder.imageButton.setOnClickListener(view -> {
            // Creates a builder for an alert dialog.
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            // Sets up the builder.
            builder.setMessage("Are you sure you want to delete the item, \"" + image.getName() + "\"?")
                    .setPositiveButton("Yes", (dialog, id) -> {
                        // Gets the position of the item (in case it has changed)
                        int pos = items.indexOf(image);
                        // Removes the item from the database.
                        Future<Boolean> success = repository.deleteImage(image.getName());
                        try {
                            // If the item was successfully removed...
                            if (success.get()) {
                                // Makes the item disappear from the view.
                                notifyItemRemoved(pos);
                            }
                        } catch (ExecutionException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        Toast.makeText(view.getContext(),"The item \"" + image.getName() + "\" has been deleted.",
                                Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", (dialog, id) -> {
                        // Cancels the dialog.
                        dialog.cancel();
                    });

            // Creates the alert dialog.
            AlertDialog alert = builder.create();
            // Sets the title.
            alert.setTitle("Deletion prompt");
            // Shows the alert dialog.
            alert.show();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<Image> getItems() {
        return items;
    }

    public void setItems(List<Image> items) {
        this.items = items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public ImageButton imageButton;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.item_image);
            this.textView = (TextView) itemView.findViewById(R.id.item_name);
            this.imageButton = (ImageButton) itemView.findViewById(R.id.item_delete);
        }
    }
}