@Echo Off
java --module-path "../MusicPlayer/lib/javafx-sdk-18.0.1/lib" --add-modules javafx.controls,javafx.fxml --add-modules javafx.controls,javafx.media -jar MusicPlayer.jar
pause