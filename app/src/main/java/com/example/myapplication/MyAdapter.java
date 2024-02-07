package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// MyAdapter is responsible for providing views that represent items in a data set.
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    // Instance of the singleton Database class to access the list of animals.
    private final Database database = Database.getInstance();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item of the RecyclerView.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_me, parent, false);
        Log.d(TAG, "onCreateViewHolder: Test");
        // Create and return a new ViewHolder object, linking it with this adapter.
        return new MyViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Bind the data at the specified position in the data set to the holder.
        Animal animal = database.getAnimal(position);
        holder.txtAnimalNames.setText(animal.getName());
        holder.imgImage.setImageURI(animal.getImage());
    }

    @Override
    public int getItemCount() {
        // Return the total number of items in the data set held by the adapter.
        return database.getDatabase().size();
    }

    // MyViewHolder describes an item view and metadata about its place within the RecyclerView.
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtAnimalNames;
        ImageView imgImage;
        private MyAdapter adapter;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize the views for this ViewHolder.
            txtAnimalNames = itemView.findViewById(R.id.txtAnimalNames);
            imgImage = itemView.findViewById(R.id.imgImage);

            // Set an OnClickListener for the delete button within the item view.
            itemView.findViewById(R.id.btnDelete).setOnClickListener(view -> {
                // Remove the item at the current position from the database and notify the adapter.
                adapter.database.getDatabase().remove(getAdapterPosition());
                adapter.notifyItemRemoved(getAdapterPosition());
            });
        }

        // Links this ViewHolder to the current instance of MyAdapter.
        public MyViewHolder linkAdapter(MyAdapter adapter) {
            this.adapter = adapter;
            return this;
        }
    }
}
