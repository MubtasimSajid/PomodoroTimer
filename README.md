# Pomodoro Timer
A GUI **timer** app to utilise the Pomodoro technique of productivity, built with JavaFX & SceneBuilder. It can help you stay focused by splitting your workflow into intervals punctuated by breaks. The user has the liberty to pick the durations & no. of cycles.

## Features
- Custom duration for work sessions, short breaks, longer breaks & number of cycles to get to a long break.
- Visual & auditory alert when switching between states
- Pause & Reset control options
- Dark & light modes
- Progress bar to visually indicate the timedown status

## How to Run
### Requirements
- Java JDK 17 or later
- JavaFX SDK (add it to your 'lib/' folder if the current folder doesn't work with your setup)
- VS Code or IntelliJ with JavaFX support (tested for VS Code, the setup is given in the .vscode folder)

## Screenshots
![Welcome Page - Light](https://raw.githubusercontent.com/MubtasimSajid/PomodoroTimer/main/Screenshots/WelcomeScreen_light.png)
![Welcome Page - Dark](https://raw.githubusercontent.com/MubtasimSajid/PomodoroTimer/tree/main/Screenshots/WelcomeScreen_dark.png)
![Countdown for Work](https://raw.githubusercontent.com/MubtasimSajid/PomodoroTimer/tree/main/Screenshots/WorkTimer_dark.png)
![Countdown for Break](https://raw.githubusercontent.com/MubtasimSajid/PomodoroTimer/tree/main/Screenshots/ShortBreak_light.png)

## Folder Structure
```
PomodoroTimer/
├── src/
│ ├── App.java
│ ├── TimerController.java
│ ├── StartingController.java
│ ├── alarm.wav
│ ├── startingScene.fxml
│ ├── timerScene.fxml
│ └── dark-theme.css
├── lib/
│ └── javafx SDK jars here
├── .vscode/
│ └── launch.json (JavaFX run config)
│ └── settings.json
└── README.md
```

## License
This project is licensed under the **GNU General Public License v3.0 (GPLv3)**.
