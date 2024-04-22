package com.example.myapplication.database;




import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.io.ByteArrayOutputStream;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

import com.example.myapplication.model.Animal;

/**
 * Repository class for handling data operations for Animal entities.
 * This class abstracts the access to multiple data sources.
 */
public class AnimalRepository {
    private MutableLiveData<List<Animal>> searchResults =
            new MutableLiveData<>();
    private LiveData<List<Animal>> allAnimals;
    private AnimalDAO animalDAO;


    public AnimalRepository(Application application) {
        AnimalRoomDatabase db = AnimalRoomDatabase.getDatabase(application);
        animalDAO = db.animalDAO();
        allAnimals = animalDAO.getAll();
    }

    public LiveData<List<Animal>> getAllAnimals() {
        return allAnimals;
    }

    public void insertAnimal(Animal animal) {
        InsertAsyncTask task = new InsertAsyncTask(animalDAO);
        task.execute(animal);
    }

    public void deleteAnimal(Animal animal) {
        DeleteAsyncTask task = new DeleteAsyncTask(animalDAO);
        task.execute(animal);
    }

    public void findAnimal(String name) {
        QueryAsyncTask task = new QueryAsyncTask(animalDAO);
        task.delegate = this;
        task.execute(name);
    }

    public int getAnimalCount() {
        return animalDAO.countAnimals();
    }



    private void asyncFinished(List<Animal> results) {
        searchResults.setValue(results);
    }


    private static class QueryAsyncTask extends
            AsyncTask<String, Void, List<Animal>> {

        private AnimalDAO asyncTaskDao;
        private AnimalRepository delegate = null;

        QueryAsyncTask(AnimalDAO dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected List<Animal> doInBackground(final String... params) {
            return asyncTaskDao.find(params[0]);
        }

        @Override
        protected void onPostExecute(List<Animal> result) {
            delegate.asyncFinished(result);
        }
    }

    private static class InsertAsyncTask extends AsyncTask<Animal, Void, Void> {

        private AnimalDAO asyncTaskDao;

        InsertAsyncTask(AnimalDAO dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Animal... params) {
            asyncTaskDao.insertAll(params[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Animal, Void, Void> {

        private AnimalDAO asyncTaskDao;

        DeleteAsyncTask(AnimalDAO dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Animal... params) {
            asyncTaskDao.delete(params[0]);
            return null;
        }
    }
}
