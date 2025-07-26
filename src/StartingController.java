import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

public class StartingController {
    @FXML
    private Spinner<Integer> workDurationSpinner;
    @FXML
    private Spinner<Integer> breakDurationSpinner;
    @FXML
    private Spinner<Integer> noOfCyclesSpinner;
    @FXML
    private Spinner<Integer> longBreakDurationSpinner;
    @FXML
    private ChoiceBox<String> themeChoiceBox;
    @FXML
    private Button startButton;
    @FXML
    private Button quitButton;

    private String selectedTheme = "light";

    @FXML
    public void initialize() {
        setupSpinner(workDurationSpinner, 1, 60, 25);
        setupSpinner(breakDurationSpinner, 1, 60, 5);
        setupSpinner(longBreakDurationSpinner, 1, 60, 20);
        setupSpinner(noOfCyclesSpinner, 1, 10, 4);

        themeChoiceBox.getItems().addAll("Light", "Dark");
        themeChoiceBox.setValue("Light");
        themeChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            selectedTheme = newValue;
            Scene scene = themeChoiceBox.getScene();
            if (scene != null) {
                if ("Dark".equalsIgnoreCase(newValue)) {
                    if (!scene.getStylesheets().contains(getClass().getResource("dark-theme.css").toExternalForm())) {
                        scene.getStylesheets().add(getClass().getResource("dark-theme.css").toExternalForm());
                    }
                } else {
                    scene.getStylesheets().remove(getClass().getResource("dark-theme.css").toExternalForm());
                }
            }
        });
    }

    private void setupSpinner(Spinner<Integer> spinner, int min, int max, int initial) {
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initial));
        spinner.setEditable(true);
        spinner.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            try {
                int value = Integer.parseInt(newValue);
                if (value < min) {
                    spinner.getValueFactory().setValue(min);
                } else if (value > max) {
                    spinner.getValueFactory().setValue(max);
                } else {
                    spinner.getValueFactory().setValue(value);
                }
            } catch (NumberFormatException e) {
                
            }
        });
    }

    @FXML
    private void handleStart() {
        int work = workDurationSpinner.getValue(), shortBreak = breakDurationSpinner.getValue(), longBreak = longBreakDurationSpinner.getValue(), cycles = noOfCyclesSpinner.getValue();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("timerScene.fxml"));
            Parent root = loader.load();
            TimerController controller = loader.getController();
            controller.initPomodoroSettings(work, shortBreak, longBreak, cycles);

            Stage stage = (Stage)startButton.getScene().getWindow();
            Scene scene = new Scene(root);

            if ("Dark".equalsIgnoreCase(selectedTheme)) {
                scene.getStylesheets().add(getClass().getResource("dark-theme.css").toExternalForm());
            }

            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleQuit() {
        Stage stage = (Stage) quitButton.getScene().getWindow();
        stage.close();
    }
}