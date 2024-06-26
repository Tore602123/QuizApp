# APK Analysis

During the testing process, two APK are used:

1. **App APK (`app-debug.apk`)**: This is the debug version of our application, which includes all the app's compiled code, resources, and manifest file. This APK is what you would typically install on a device during development and testing phases.

2. **Test APK (`app-debug-androidTest.apk`)**: This is a separate APK dedicated to running the tests. It contains the compiled test code, including any test-specific resources and dependencies.

## APK Decoding and interpration with `apktool`

1. **Loading Resource Table**: The tool starts by reading binary XML files that list all the resources in the APK, like images and text. This helps `apktool` identify and use these resources properly.

2. **Decoding Resources**: Next, it pulls out and converts all the resources from the APK, such as pictures and XML layouts, found in the `res` folder.

3. **Decoding Value XMLs**: It also decodes files in the `values` folder that contain text strings, sizes, and style settings into a format that's easy to read.

4. **Manifest Decoding**: The tool extracts and decodes the `AndroidManifest.xml`, which is important because it includes details about the app's setup, permissions, and components like activities and services.

5. **Decoding `.dex` Files**: `apktool` also works on `.dex` files, turning them into a more understandable format called `smali`. This is like a middle-step version of the app's original code. If the app has multiple `.dex` files, it means it's using a method to support more code than usual.

6. **Copying Assets and Libs**: The tool copies other materials like raw assets and native libraries that are part of the APK.

7. **Handling Unknown and Original Files**: Finally, `apktool` keeps any files it doesn’t directly process, like custom files specific to the app, unchanged.

# Test Documentation

### 1. `MainActivityTest`

**Objective**: Ensure correct navigation from the `MainActivity` to other key activities within the app, specifically `QuizActivity` and `DatabaseActivity`.

**Environment Setup**:
- Before each test, the `Intents` framework is initialized to intercept and validate intents used within the tests. The `MainActivity` is launched in an isolated environment using `ActivityScenario`.

**Test Methods**:
- `testNavigationToQuizActivity()`: Verifies that clicking the quiz button properly initiates an intent to navigate to `QuizActivity`. This is crucial to confirm that the application's flow from the main menu to the quiz section functions as intended.
- `testNavigationToDatabaseActivity()`: Checks that clicking the database button correctly triggers an intent to open `DatabaseActivity`. This test ensures that the user can access the database features from the main menu correctly.

**Testing Steps**:
- **Common Setup**: Each test begins by launching the `MainActivity` through `ActivityScenario`, which allows for real-time interaction and monitoring.
- **Action and Verification**:
  - For navigation tests, `Espresso` simulates a button click.
  - `Intents` is then used to verify that the correct component (activity) is targeted by the resulting intent.
- **Teardown**: After each test, resources are cleaned up by releasing the `Intents` framework and closing the activity scenario to ensure a fresh start for subsequent tests.

**Methodology**:
- Utilizes `Espresso` for simulating user interactions such as button clicks.
- Employs `Intents` to intercept and assert properties of intents, verifying that the correct navigation occurs.
- `ActivityScenario` manages the lifecycle of the activity under test, ensuring that the environment mirrors a real-world usage scenario.

### 2. `DatabaseActivityTest`

**Objective**: To verify that the image management functionality within `DatabaseActivity` operates as expected, including adding and deleting images from the database.

**Environment Setup**:
- Each test starts by initializing the `Intents` framework and launching `DatabaseActivity`, providing a clean state for each test case.

**Test Methods**:
- `testAddImage()`: Tests the functionality of adding an image to the database. This test simulates the entire flow of selecting an image from a gallery and adding it with a specific name (e.g., "Cat"). It uses mocked intents to simulate gallery selection and verifies the increment in the image count within the database.
- `testDeleteImage()`: Confirms the ability to delete an image from the database. It simulates the user action of deleting an image named "Cat" and checks if the database's image count decrements accordingly.

**Testing Steps**:
- **Add Image Test**:
  - Observes the initial number of images in the database.
  - Simulates clicking the add button to trigger the image addition UI.
  - Mocks an intent for selecting an image and simulates the response from the gallery.
  - Enters the image name and confirms the addition through the UI.
  - Observes the number of images post-addition and asserts the expected increase.
- **Delete Image Test**:
  - Captures the initial state of the database for reference.
  - Simulates the user action of deleting a previously added image by interacting with UI elements directly associated with the image (e.g., delete button next to the image’s name).
  - Confirms the deletion action via the UI and observes the new state of the database.
  - Verifies that the image count reflects the deletion accurately.

**Methodology**:
- Utilizes `Espresso` for UI interactions such as clicking and text entry.
- Employs `ActivityScenario` to manage and test activity states.
- Implements intent mocking with `Intents.test()` to control external dependencies like image gallery access.

### 3. `QuizActivityTest`

**Objective**: Verify the functionality of option selection in `QuizActivity`, specifically ensuring that the counters for correct and total attempts are updated accurately based on user interactions.

**Environment Setup**:
- Each test initializes `QuizActivity` with a predefined difficulty setting (`easy`). This setup ensures a consistent testing environment where the difficulty level influences the behavior being tested.

**Test Methods**:
- `test_correctOptionPicked()`: Confirms that selecting the correct answer increments both the correct answer counter and the attempt counter by 1. This test simulates user interaction by programmatically clicking the correct option button.
- `test_wrongOptionPicked()`: Ensures that selecting an incorrect answer increments only the attempt counter by 1, while the correct answer counter remains unchanged. This test involves simulating a click on an incorrect option button.

**Methodology**:
- Each test uses `ActivityScenario` for controlling and testing the activity under real conditions.
- Tests are executed within a separate thread in the activity's context, allowing for real-time interaction testing and state verification.
- `Espresso` is utilized for UI interactions, such as clicking buttons corresponding to answer choices.

**Utility Methods**:
- `getChoiceForCorrectOption(int correctOption)`: Returns the UI element ID of the correct answer option based on its index. This method is crucial for testing correct answer selection.
- `getChoiceForIncorrectOption(int correctOption)`: Provides the UI element ID of an incorrect option, ensuring the test avoids the correct answer for validating error handling.



