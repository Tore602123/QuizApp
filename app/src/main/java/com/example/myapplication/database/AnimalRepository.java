package com.example.myapplication.database;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import android.os.AsyncTask;

import com.example.myapplication.model.Animal;

/**
 * Repository class for handling data operations for Animal entities.
 * This class abstracts the access to multiple data sources.
 */
public class AnimalRepository {

    private MutableLiveData<List<Animal>> searchResults = new MutableLiveData<>();
    private LiveData<List<Animal>> allAnimals;
    private AnimalDAO animalDAO;

    /**
     * Constructor that gets a database instance and initializes the DAO.
     * @param application The application context used to get the database.
     */
    public AnimalRepository(Application application) {
        AnimalRoomDatabase db = AnimalRoomDatabase.getDatabase(application);
        animalDAO = db.animalDAO();
        allAnimals = animalDAO.getAll();
    }

    /**
     * Retrieves all animals from the database.
     * @return A LiveData list of all animals.
     */
    public LiveData<List<Animal>> getAllAnimals() {
        return allAnimals;
    }

    /**
     * Retrieves the results of a search query.
     * @return A MutableLiveData list of animals matching search criteria.
     */
    public MutableLiveData<List<Animal>> getSearchResults() {
        return searchResults;
    }

    /**
     * Inserts an animal into the database asynchronously.
     * @param animal The animal to be inserted.
     */
    public void insertAnimal(Animal animal) {
        new InsertAsyncTask(animalDAO).execute(animal);
    }

    /**
     * Deletes an animal from the database asynchronously.
     * @param animal The animal to be deleted.
     */
    public void deleteAnimal(Animal animal) {
        new DeleteAsyncTask(animalDAO).execute(animal);
    }

    /**
     * Finds animals by name asynchronously and updates searchResults LiveData.
     * @param name The name to search for.
     */
    public void findAnimal(String name) {
        new QueryAsyncTask(animalDAO, this).execute(name);
    }

    /**
     * Callback method to update search results after a query execution.
     * @param results The list of animals found.
     */
    private void asyncFinished(List<Animal> results) {
        searchResults.setValue(results);
    }

    /**
     * AsyncTask for querying the database by name.
     */
    private static class QueryAsyncTask extends AsyncTask<String, Void, List<Animal>> {
        private AnimalDAO asyncTaskDao;
        private AnimalRepository delegate;

        QueryAsyncTask(AnimalDAO dao, AnimalRepository delegate) {
            this.asyncTaskDao = dao;
            this.delegate = delegate;
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

    /**
     * AsyncTask for inserting an animal into the database.
     */
    private static class InsertAsyncTask extends AsyncTask<Animal, Void, Void> {
        private AnimalDAO asyncTaskDao;

        InsertAsyncTask(AnimalDAO dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Animal... params) {
            asyncTaskDao.insertAll(params[0]);
            return null;
        }
    }

    /**
     * AsyncTask for deleting an animal from the database.
     */
    private static class DeleteAsyncTask extends AsyncTask<Animal, Void, Void> {
        private AnimalDAO asyncTaskDao;

        DeleteAsyncTask(AnimalDAO dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Animal... params) {
            asyncTaskDao.delete(params[0]);
            return null;
        }
    }
}