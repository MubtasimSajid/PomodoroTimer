import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class TimerController {
    @FXML
    private Label modeLabel;
    @FXML
    private Label timerLabel;
    @FXML
    private ProgressBar progressIndicator;
    @FXML
    private Button pauseButton;
    @FXML
    private Button resetButton;

    private Timeline timeline;
    private int sessionSeconds, totalSessionSeconds, currentCycle = 0, workDuration, shortBreak, longBreak, cycles;
    private boolean isWorkSession = true, isPaused = false;

    private Clip alarmSound;

    @FXML
    public void initialize() {
        try {
            var url = getClass().getResource("/alarm.wav");

            if (url == null) {
                System.err.println("alarm.wav not found in classpath!");
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            alarmSound = AudioSystem.getClip();
            alarmSound.open(audioIn);

        } catch (Exception e) {
            System.err.println("Error loading alarm.wav: " + e.getMessage());
        }
    }

    public void initPomodoroSettings(int work, int sb, int lb, int totalCycles) {
        this.workDuration = work;
        this.shortBreak = sb;
        this.longBreak = lb;
        this.cycles = totalCycles;

        startWorkSession();
    }

    private void startWorkSession() {
        isWorkSession = true;
        modeLabel.setText("Work Time!");
        modeLabel.setFont(Font.font(modeLabel.getFont().getFamily(), 32));
        progressIndicator.setStyle("-fx-accent: #4caf50;");
        totalSessionSeconds = workDuration * 60;
        startTimer();
    }

    private void startBreakSession() {
        isWorkSession = false;
        currentCycle++;

        if (currentCycle % cycles == 0) {
            modeLabel.setText("Long Break");
            totalSessionSeconds = longBreak * 60;
        } else {
            modeLabel.setText("Short Break");
            totalSessionSeconds = shortBreak * 60;
        }
        modeLabel.setFont(Font.font(modeLabel.getFont().getFamily(), 32));
        progressIndicator.setStyle("-fx-accent: #ff9800;");
        startTimer();
    }

    private void startTimer() {
        if (timeline != null) {
            timeline.stop();
        }
        sessionSeconds = totalSessionSeconds;
        updateTimerLabel();

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> tick()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void showVisualNotifier(Color flashColor) {
        flashBackground(flashColor);
        shakeNode(modeLabel);
        animateModeLabel();
        animateProgressBar();
    }

    private void flashBackground(Color flashColor) {
        Parent root = pauseButton.getScene().getRoot();
        if (root instanceof Pane) {
            Pane pane = (Pane) root;
            Background originalBg = pane.getBackground();
            Background flashBg = new Background(new BackgroundFill(flashColor, CornerRadii.EMPTY, null));
            pane.setBackground(flashBg);

            PauseTransition pause = new PauseTransition(Duration.seconds(0.4));
            pause.setOnFinished(e -> pane.setBackground(originalBg));
            pause.play();
        }
    }

    private void shakeNode(Node node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(60), node);
        tt.setFromX(0);
        tt.setByX(10);
        tt.setCycleCount(6);
        tt.setAutoReverse(true);
        tt.play();
    }

    private void animateModeLabel() {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO,
                new KeyValue(modeLabel.textFillProperty(), Color.YELLOWGREEN),
                new KeyValue(modeLabel.fontProperty(), Font.font(modeLabel.getFont().getFamily(), 32))
            ),
            new KeyFrame(Duration.seconds(0.5),
                new KeyValue(modeLabel.textFillProperty(), Color.BLACK),
                new KeyValue(modeLabel.fontProperty(), Font.font(modeLabel.getFont().getFamily(), 24))
            )
        );
        timeline.setOnFinished(e -> {
            modeLabel.setFont(Font.font(modeLabel.getFont().getFamily(), 32));
        });
        timeline.play();
    }

    private void animateProgressBar() {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(progressIndicator.progressProperty(), 1.0)),
            new KeyFrame(Duration.seconds(0.4), new KeyValue(progressIndicator.progressProperty(), 0.0))
        );
        timeline.play();
    }

    private void tick() {
        if (sessionSeconds <= 0) {
            timeline.stop();
            if (alarmSound != null) {
                if (alarmSound.isRunning())
                    alarmSound.stop();
                alarmSound.setFramePosition(0);
                alarmSound.start();
            }
            if (isWorkSession) {
                showVisualNotifier(Color.BLUE);
                startBreakSession();
            } else {
                showVisualNotifier(Color.RED);
                startWorkSession();
            }
            return;
        }

        sessionSeconds--;
        updateTimerLabel();
        double progress = 1.0 - ((double) sessionSeconds / totalSessionSeconds);
        progressIndicator.setProgress(progress);
    }

    private void updateTimerLabel() {
        int min = sessionSeconds / 60, sec = sessionSeconds % 60;
        timerLabel.setText(String.format("%02d:%02d", min, sec));
    }

    @FXML
    private void handlePause() {
        if (timeline == null)
            return;

        if (isPaused) {
            timeline.play();
            pauseButton.setText("Pause");
        } else {
            timeline.pause();
            pauseButton.setText("Resume");
        }

        isPaused = !isPaused;
    }

    @FXML
    private void handleReset() {
        if (timeline != null) {
            timeline.stop();
        }

        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/startingScene.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            javafx.stage.Stage stage = (javafx.stage.Stage) resetButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            System.err.println("Error loading startingScene.fxml: " + e.getMessage());
        }
    }
}
