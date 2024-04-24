package com.example.myapplication.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.util.DatabaseUtil;
import com.example.myapplication.util.Util;
import com.example.myapplication.viewmodel.MainViewModel;


/**
 * MainActivity serves as the launch activity for the application,
 * allowing navigation to other activities and managing quiz difficulty settings.
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String difficulty = "easy";
    private MainViewModel mainViewModel;

    /**
     * Called when the activity is starting. This is where most initialization should go:
     * calling setContentView(int) to inflate the activity's UI, using findViewById(int)
     * to programmatically interact with widgets in the UI, calling ViewModelProvider to
     * retrieve ViewModel instances, etc.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the most recent data,
     * otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initializeViewModel();
        setupToolbar();
        setupButtons();
    }

    /**
     * Initializes the main view model for the activity. Observes changes to the image list
     * and populates the database with default data if it's empty.
     */
    private void initializeViewModel() {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getAllImages().observe(this, images -> {
            if (images.isEmpty()) {
                mainViewModel.insertSeveral(DatabaseUtil.getDefaults(getResources()));
            }
        });
    }

    /**
     * Sets up the toolbar by inflating the menu resource into it.
     */
    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
    }

    /**
     * Configures the buttons for navigation and functionality, including setting event listeners
     * for user interactions like button clicks and switch toggles.
     */
    private void setupButtons() {
        binding.content.difficultySwitch.setOnCheckedChangeListener(this::switchDifficulty);
        binding.content.quizButton.setOnClickListener(view ->
                Util.startActivity(MainActivity.this, QuizActivity.class, "difficulty", difficulty));
        binding.content.dbButton.setOnClickListener(view ->
                Util.startActivity(MainActivity.this, DatabaseActivity.class));
    }

    /**
     * Inflates the menu for this activity. This adds items to the action bar if it is present.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Handles action bar item clicks here. The action bar will automatically handle clicks on the
     * Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            // Here you might open settings activity or perform other action
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Toggles the difficulty of the quiz between "easy" and "hard".
     * This method is linked to a switch and will change the state of `difficulty` accordingly.
     *
     * @param unusedObject An unused parameter for method reference compatibility.
     * @param isHardMode Whether the "hard" difficulty should be activated.
     */
    private void switchDifficulty(Object unusedObject, boolean isHardMode) {
        difficulty = isHardMode ? "hard" : "easy";
    }
}