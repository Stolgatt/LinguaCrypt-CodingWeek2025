package linguacrypt.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class TimerController {

    private Timeline timeline;
    private int timeRemaining; // Time left in seconds
    private Label timerLabel;
    private Runnable onTimerEnd;

    // Constructor to initialize the timer
    public TimerController(Label timerLabel, int initialTime) {
        this.timerLabel = timerLabel;
        this.timeRemaining = initialTime;
        updateLabel();
    }

    // Start the timer
    public void startTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeRemaining--;
            updateLabel();

            if (timeRemaining <= 0) {
                stopTimer();
                if (onTimerEnd != null) {
                    onTimerEnd.run();
                }
            }
        }));

        timeline.setCycleCount(Timeline.INDEFINITE); // Infinite updates until stopped
        timeline.play();
    }

    // Stop the timer
    public void stopTimer() {
        if (timeline != null) {
            timeline.stop();
        }
    }


    // Update the label with the remaining time
    public void updateLabel() {
        if (timeRemaining < 0) {
            timerLabel.setText("∞");
            return;    
        }
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds)); // Format MM:SS
    }

    // Reset the timer with a new time
    public void resetTimer(int newTime) {
        stopTimer();
        this.timeRemaining = newTime;
        updateLabel();
    }

    // Set the action when timer's end
    public void setOnTimerEnd(Runnable onTimerEnd) {
        this.onTimerEnd = onTimerEnd;
    }

}
