package Business.Java;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.Timer;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSlider;

import Data.musicFile;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;

public class controllerSceneHome implements Initializable {
    @FXML
    private Label nameSong, increaseTime, fullTime, lb_Search, lb_title;

    @FXML
    private AnchorPane pane;

    @FXML
    private JFXSlider timeProgress, changeVolumne;

    @FXML
    private Button btn_Play, btn_Pause;

    @FXML
    private Button btn_Pre;

    @FXML
    private Button btn_Next;

    @FXML
    private Button btn_shuffle, btn_unShuffle;

    @FXML
    private Button btn_autoNext, btn_unAutoNext;

    @FXML
    private Button btn_sound, btn_mute;

    @FXML
    private JFXListView listView;

    @FXML
    private TextField searchBox;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private Line line;

    private File file;
    private String directoryFile;
    private String parentFile;
    private Media media;
    private MediaPlayer mediaPlayer;

    private Timer timer;

    private screenParameter screenParameter;
    private boolean runnningMusic = false;
    private int minute = 0;
    private int second = 0;
    private int minuteFullTime;
    private int secondFullTime;
    private int indexFocus = -1;
    private ArrayList<String> nameMusic;
    private DecimalFormat decimalFormat = new DecimalFormat("0.0");
    private int numChange = -1;
    private boolean isChange = false;

    private Image img_icon;
    private ImageView imageView_icon;
    private double volomne;
    private boolean isShuffStatus = false;
    private randomMusic randomMusic;
    private int nextMusicShuffle = 0;
    private boolean isAutoNext = false;
    private double height_item = 33.2;
    private int numRow = 0;
    private boolean isPre = false;
    private manageFile manageFile;
    private boolean initialMusicButton = false;
    private String nameIsChoose = "";

    private manageMusicList manageMusicList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setParameter();
        manageFile = new manageFile();
        if (manageFile.isUrlFile()) {
            manageFile.initializationListFile();
            parentFile = manageFile.getParentFile();
            manageMusicList = new manageMusicList(manageFile.getListName());
            displayListMusic("", manageFile.getListName());
            initialMusicButton = true;
        }

    }

    public void setCloseButton() {
        if (parentFile != null) {
            manageFile.writeFile(parentFile);
        }
        Platform.exit();
        System.exit(0);
    }

    // invoke button play
    public void playMusic() {
        if (initialMusicButton) {
            initialMusicButton = false;
            runnningMusic = true;
            autoNext();
            listView.getSelectionModel().select(0);
            file = new File(parentFile + listView.getItems().get(0));
            media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            timeProgress.setDisable(false);
            timeProgress();
        }

        if (mediaPlayer != null && !runnningMusic && !isShuffStatus && !isAutoNext) {
            stopMusic();
        }
        if (!runnningMusic) {
            file = new File(directoryFile);
            media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            changeStatus(btn_Play, btn_Pause);
            timeProgress.setDisable(false);
            timeProgress();
        }

        if (runnningMusic)
            changeStatus(btn_Play, btn_Pause);

        if (timer != null)
            timer.start();

        if (!initialMusicButton)
            mediaPlayer.play();

    }

    // invoked button pause
    public void pauseMusic() {
        changeStatus(btn_Pause, btn_Play);
        timer.stop();
        runnningMusic = true;
        mediaPlayer.pause();
    }

    // invoked when mudicMedia done
    public void stopMusic() {
        runnningMusic = false;
        timer.stop();
        mediaPlayer.seek(Duration.seconds(0.0));
        timeProgress.setValue(0.0);
        timeProgress.setDisable(true);
        second = 0;
        minute = 0;
        Platform.runLater(() -> increaseTime.setText("00:00"));
        changeStatus(btn_Pause, btn_Play);
        mediaPlayer.stop();
    }

    // run time bar
    public void timeProgress() {

        timer = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                // TODO Auto-generated method stub
                double start = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                double seconds = start;
                double minute = media.getDuration().toMinutes();
                timeProgress.setMax(media.getDuration().toSeconds());
                timeProgress.setValue(start);
                displayTime(seconds, minute);

                if (start == end) {
                    stopMusic();

                    if (isShuffStatus || isAutoNext) {
                        numChange = listView.getSelectionModel().getSelectedIndex() + 1;
                        isChange = true;
                        selectMusic();
                    }
                }

            }

        });
    }

    // display full and increase time
    private void displayTime(double seconds, double minutes) {
        convertTimeProgress(0);
        convertFullTime(minutes);
        String secondText;
        if (second < 10) {
            secondText = "0" + Integer.toString(second);
        } else {
            secondText = Integer.toString(second);
        }

        String increaseMinuteText = Integer.toString(minute);

        String minuteFullTimeText;
        if (minuteFullTime < 10) {
            minuteFullTimeText = "0" + Integer.toString(minuteFullTime);
        } else {
            minuteFullTimeText = Integer.toString(minuteFullTime);
        }

        String secondFullTimeText;
        if (secondFullTime < 10) {
            secondFullTimeText = "0" + Integer.toString(secondFullTime);
        } else {
            secondFullTimeText = Integer.toString(secondFullTime);
        }

        fullTime.setVisible(true);
        Platform.runLater(() -> fullTime.setText(minuteFullTimeText + ":" + secondFullTimeText));
        Platform.runLater(() -> increaseTime.setText(increaseMinuteText + ":" + secondText));

        if (minute * 60 + second > minuteFullTime * 60 + secondFullTime) {
            Platform.runLater(() -> increaseTime.setText((minuteFullTimeText + ":" + secondFullTimeText)));
        }
    }

    // making music time
    private void convertFullTime(double minutes) {
        minuteFullTime = (int) Double.parseDouble(decimalFormat.format(minutes)) / 1;
        secondFullTime = (int) (Double.parseDouble(decimalFormat.format(minutes)) % 1 * 60);
    }

    // making time progress
    private void convertTimeProgress(double timeSkip) {
        if (timeSkip > 0) {

            double minuteSkip = Double.parseDouble(decimalFormat.format(timeSkip)) / 60;
            if ((int) (minuteSkip / 1) > 0) {
                minute = (int) minuteSkip / 1;
                second = (int) (minuteSkip % 1 * 60);
            } else {
                minute = 0;
                second = (int) (Double.parseDouble(decimalFormat.format(timeSkip)) / 1);
            }
        }

        if (timeSkip == 0) {
            second += 1;
            if (second == 60) {
                minute += 1;
                second = 0;
            }
        }
    }

    // invoked when click timebar
    public void isClicked(MouseEvent e) {
        mediaPlayer.seek(Duration.seconds(timeProgress.getValue()));
        convertTimeProgress(timeProgress.getValue());
    }

    /**
     * call manageFile class
     */
    public void openFolder() {
        manageFile.openSelectFolder();
        parentFile = manageFile.getParentFile();
        manageMusicList = new manageMusicList(manageFile.getListName());
        displayListMusic("", manageMusicList.getOriginalMusic());
    }

    /**
     * call manageFile class
     */
    public void openFileMusic() {
        manageFile.openSelectFile();
        parentFile = manageFile.getParentFile();
        manageMusicList = new manageMusicList(manageFile.getListName());
        displayListMusic(manageFile.getNameFileChooser(), manageMusicList.getOriginalMusic());
    }

    private void updateListView(String[] str, int index) {
        listView.getItems().clear();
        listView.getItems().addAll(str);
        if (index != -1)
            listView.getSelectionModel().select(index);
    }

    /*
     * dis play Listview
     * set music when choosed at openFileMusic
     */
    private void displayListMusic(String nameChoose, String[] listFileName) {
        nameMusic = new ArrayList<>();
        for (String s : listFileName) {
            nameMusic.add(s);
            if (!nameChoose.equals("") && nameChoose.toLowerCase().equals(s.toLowerCase())) {
                indexFocus = nameMusic.size() - 1;
            }
        }

        listView.getItems().clear();
        listView.getItems().addAll(nameMusic);
        changeSizeListView(nameMusic);
        visibles(listView, lb_Search, lb_title, searchBox);
        setPosChoiceBox(0, 30);
        timeProgress.setDisable(false);
        if (indexFocus != -1) {
            listView.getSelectionModel().select(indexFocus);
            numChange = listView.getSelectionModel().getSelectedIndex();
            isChange = true;
            selectMusic();
        }
    }

    /**
     * 
     * @param arr
     * 
     *            change size listView when ArrList has changed
     *            add items " " to ArrayList if size of ArrayList less than 3
     */
    private void changeSizeListView(ArrayList<String> arr) {
        int size = arr.size();
        if (size < numRow) {
            if (size < 3) {
                for (int i = 0; i < 3 - size; i++) {
                    arr.add("");
                }
                listView.getItems().clear();
                listView.getItems().addAll(arr);
            }
            listView.setMinHeight(arr.size() * height_item);
        } else {
            listView.setMinHeight(screenParameter.getScreenHeight() * 60 / 100);
        }
    }

    /*
     * set parameter before play
     * invoked when listView cell is choosed
     * invoked when music from others active:
     * * click next button
     * * click previous button
     */
    @FXML
    void selectMusic() {
        if (mediaPlayer != null)
            stopMusic();

        if (isChange) {
            if (numChange == -1) {
                numChange = this.nameMusic.size() - 1;
            }

            if (numChange == this.nameMusic.size()) {
                numChange = 0;
            }

            listView.getSelectionModel().select(numChange);

            isChange = false;
        }

        nameIsChoose = (String) listView.getSelectionModel().getSelectedItem();
        directoryFile = parentFile + nameIsChoose;

        Platform.runLater(() -> setNameLabelSong(nameIsChoose));
        runnningMusic = false;
        isPre = false;
        initialMusicButton = false;

        if (!isChange && isShuffStatus)
            listView.getSelectionModel().select(manageMusicList.getIndexMusicFromRecentList(nameIsChoose));
        else
            listView.getSelectionModel().select(manageMusicList.getIndexMusicFromListOriginal(nameIsChoose));
        if (nameIsChoose != null && !nameIsChoose.equals(""))
            playMusic();
    }

    /*
     * called when button previous is clicked
     */
    @FXML
    void preMusic() {
        isPre = true;
        numChange = listView.getSelectionModel().getSelectedIndex() - 1;
        isChange = true;
        stopMusic();
        selectMusic();
    }

    /*
     * called when button next is clicked
     */
    @FXML
    void nextMusic() {
        numChange = listView.getSelectionModel().getSelectedIndex() + 1;
        isChange = true;
        stopMusic();
        selectMusic();
    }

    /*
     * search music name
     * add to listView and show
     */
    @FXML
    void searchMusic() {
        listView.getItems().clear();
        ArrayList<String> nameTempt = new ArrayList<>();
        for (int i = 0; i < nameMusic.size(); i++) {
            if (nameMusic.get(i).toLowerCase().startsWith(searchBox.getText().toLowerCase())) {
                nameTempt.add(nameMusic.get(i));
            }
        }

        listView.getItems().addAll(nameTempt);
        listView.getSelectionModel().select(manageMusicList.getIndexMusicFromListOriginal(nameIsChoose));
        changeSizeListView(nameTempt);
    }

    /*
     * @param btn1
     * 
     * @param bt2
     * hide and disable bt1
     * visible and enable bt2
     */
    private void changeStatus(Button btn1, Button bt2) {
        btn1.setVisible(false);
        btn1.setDisable(true);

        bt2.setVisible(true);
        bt2.setDisable(false);
    }

    /**
     * called when button sound is cliked
     * change sound of music
     */
    @FXML
    void soundButton() {
        changeStatus(btn_sound, btn_mute);
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(0);
        }
        changeVolumne.setValue(0);
    }

    /*
     * called when button mute is cliked
     */
    @FXML
    void muteButton() {
        changeStatus(btn_mute, btn_sound);

        if (mediaPlayer != null) {
            mediaPlayer.setVolume(0.5);
        }
        changeVolumne.setValue(50);
    }

    /*
     * called when button shuffle is clicked
     * change to unShuffleStatus
     * create randomMusic class
     * making random arrayList musicName
     * isShuffStatus is true
     */
    @FXML
    void shuffleStatus() {
        changeStatus(btn_shuffle, btn_unShuffle);
        changeStatus(btn_unAutoNext, btn_autoNext);
        if (nameMusic != null) {
            randomMusic = new randomMusic(nameIsChoose, nameMusic);
            randomMusic.randdom();
            manageMusicList.setRecentList(randomMusic.getListNameRan());

            updateListView(randomMusic.getListNameRan(), -1);
            if (media != null)
                updateListView(randomMusic.getListNameRan(), 0);
        }
        nextMusicShuffle = 0;
        isShuffStatus = true;
        isAutoNext = false;
    }

    /*
     * called when button unShuffle is clicked
     * change to ShuffleStatus
     * isShuffStatus is true
     */
    @FXML
    void unShuffleStatus() {
        changeStatus(btn_unShuffle, btn_shuffle);
        changeStatus(btn_unAutoNext, btn_autoNext);

        updateListView(manageMusicList.getOriginalMusic(), manageMusicList.getIndexMusicFromListOriginal(nameIsChoose));

        isShuffStatus = false;
        isAutoNext = false;
    }

    /*
     * called when button autoNext is clikced
     * change to unAutoNext
     * isShuffStatus is false
     * isAutoNext is true
     */
    @FXML
    void autoNext() {
        changeStatus(btn_autoNext, btn_unAutoNext);
        changeStatus(btn_unShuffle, btn_shuffle);

        updateListView(manageMusicList.getOriginalMusic(), manageMusicList.getIndexMusicFromListOriginal(nameIsChoose));

        isAutoNext = true;
        isShuffStatus = false;
    }

    /*
     * called when button unAutoNext is clikced
     * change to AutoNext
     * isShuffStatus is false
     * isAutoNext is false
     */
    @FXML
    void unAutoNext() {
        changeStatus(btn_unAutoNext, btn_autoNext);
        changeStatus(btn_unShuffle, btn_shuffle);
        isAutoNext = false;
        isShuffStatus = false;
    }

    /*
     * invoked all event and Listener of class
     */
    private void invoked() {
        changeVolumne.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                // TODO Auto-generated method stub
                volomne = changeVolumne.getValue() / 100;
                if (volomne > 0) {
                    changeStatus(btn_mute, btn_sound);
                } else {
                    changeStatus(btn_sound, btn_mute);
                }

                if (mediaPlayer != null)
                    mediaPlayer.setVolume(volomne);
            }

        });
    }

    /*
     * set initializaion of choiceBox
     */
    private void initilaizeChoieBox() {
        choiceBox.getItems().addAll("Open File", "Open Folder");
        choiceBox.setValue("Open Music");
    }

    /**
     * 
     * @param e
     * 
     *          called when choiceBox cell is choosed
     *          openFileMusic() when "Open File" is clicked
     *          openFolder() when "Open Folder" is clicked
     */
    private void pick(ActionEvent e) {
        if (choiceBox.getValue().equals("Open File")) {
            openFileMusic();
        }

        if (choiceBox.getValue().equals("Open Folder")) {
            openFolder();
        }

        choiceBox.setValue("Open Music");
    }

    /**
     * 
     * @param name
     *             setText() of label nameSong
     *             visible nameSong
     *             setAlignment nameSong
     *             setFont nameSong
     */
    private void setNameLabelSong(String name) {
        nameSong.setFont(new Font(30.0));
        nameSong.setAlignment(Pos.CENTER);
        nameSong.setText((String) listView.getSelectionModel().getSelectedItem());
        nameSong.setVisible(true);
    }

    /*
     * hile all of ject is set
     * 
     * @param o
     * set array objects
     * hide objects
     */
    private void unVisables(Object... o) {
        for (Object od : o) {
            ((Node) od).setVisible(false);
        }
    }

    /*
     * visible all of ject is set
     * 
     * @param o
     * set array objects
     * setvisible objects
     */
    private void visibles(Object... o) {
        for (Object od : o) {
            ((Node) od).setVisible(true);
        }
    }

    /**
     * set image to button is set
     * 
     * @param o
     * @param nameImage
     * 
     *                  create fileImage
     *                  create Image img_icon
     *                  create ImageView imageView_icon
     *                  set image to button
     */
    private void setImage(Button o, String nameImage) {
        File fileImage = new File("src/Icon/" + nameImage);
        img_icon = new Image(fileImage.getAbsolutePath());
        imageView_icon = new ImageView(img_icon);
        o.setGraphic(imageView_icon);
    }

    /**
     * set position choiceBox and line under it
     * 
     * @param x
     * @param y
     * 
     *          set choiceBox X
     *          set choiceBox Y
     *          set line startX
     *          set line endX
     *          set line X
     *          set line Y
     */
    private void setPosChoiceBox(double x, double y) {
        choiceBox.setLayoutX(x);
        choiceBox.setLayoutY(y);
        line.setStartX(x + choiceBox.getMinWidth() - 10);
        line.setEndX(x - choiceBox.getMinWidth() + 10);
        line.setLayoutY(y + choiceBox.getMinHeight());
        line.setLayoutX(choiceBox.getMinWidth());
    }

    private void makingListViewCell() {
        int height_tempt = 0;
        while (height_tempt < listView.getMinHeight()) {
            height_tempt += height_item;
            numRow += 1;
        }
    }

    /**
     * set parameters of all elements
     */
    private void setParameter() {
        screenParameter = new screenParameter();

        pane.setMinSize(screenParameter.getScreenWidth(), screenParameter.getScreenHeight());

        /**
         * set parameter of btn_Play
         */
        btn_Play.setMinSize(40, 40);
        btn_Play.setLayoutY(screenParameter.getScreenHeight() - 100);
        btn_Play.setLayoutX(screenParameter.getScreenWidth() / 2 - btn_Play.getWidth() / 2);
        btn_Play.setFocusTraversable(false);
        btn_Play.setText("");
        setImage(btn_Play, "playIcon.png");

        /**
         * set parameter of btn_Next
         */
        btn_Next.setLayoutY(screenParameter.getScreenHeight() - 103);
        btn_Next.setLayoutX(screenParameter.getScreenWidth() / 2 - btn_Next.getWidth() / 2 + 45);
        btn_Next.setFocusTraversable(false);
        btn_Next.setText("");
        setImage(btn_Next, "nextIcon.png");

        /**
         * set parameter of btn_Pre
         */
        btn_Pre.setLayoutY(screenParameter.getScreenHeight() - 103);
        btn_Pre.setLayoutX(screenParameter.getScreenWidth() / 2 - btn_Pre.getWidth() / 2 - 60);
        btn_Pre.setFocusTraversable(false);
        btn_Pre.setText("");
        setImage(btn_Pre, "preIcon.png");

        /**
         * set parameter of btn_Pause
         */
        btn_Pause.setMinSize(40, 40);
        btn_Pause.setLayoutY(screenParameter.getScreenHeight() - 103);
        btn_Pause.setLayoutX(screenParameter.getScreenWidth() / 2 - btn_Pause.getWidth() / 2 - 5);
        btn_Pause.setFocusTraversable(false);
        btn_Pause.setText("");
        btn_Pause.setVisible(false);
        btn_Pause.setDisable(true);
        setImage(btn_Pause, "pauseIcon.png");

        /**
         * set parameter of btn_shuffle
         */
        btn_shuffle.setLayoutY(screenParameter.getScreenHeight() - 103);
        btn_shuffle.setLayoutX(screenParameter.getScreenWidth() / 2 - btn_Next.getWidth() / 2 + 100);
        btn_shuffle.setFocusTraversable(false);
        btn_shuffle.setText("");
        setImage(btn_shuffle, "shuffleIcon.png");

        /**
         * set parameter of btn_unShuffle
         */
        btn_unShuffle.setLayoutY(screenParameter.getScreenHeight() - 103);
        btn_unShuffle.setLayoutX(screenParameter.getScreenWidth() / 2 - btn_Next.getWidth() / 2 + 100);
        btn_unShuffle.setFocusTraversable(false);
        btn_unShuffle.setText("");
        setImage(btn_unShuffle, "unshuffleIcon.png");

        /**
         * set parameter of btn_autoNext
         */
        btn_autoNext.setMinSize(47, 40);
        btn_autoNext.setLayoutY(screenParameter.getScreenHeight() - 103);
        btn_autoNext.setLayoutX(screenParameter.getScreenWidth() / 2 - btn_autoNext.getMinWidth() - 80);
        btn_autoNext.setText("");
        setImage(btn_autoNext, "autoNextIcon.png");

        /**
         * set parameter of btn_unAutoNext
         */
        btn_unAutoNext.setMinSize(47, 40);
        btn_unAutoNext.setLayoutY(screenParameter.getScreenHeight() - 103);
        btn_unAutoNext.setLayoutX(screenParameter.getScreenWidth() / 2 - btn_autoNext.getMinWidth() - 80);
        btn_autoNext.setFocusTraversable(false);
        btn_unAutoNext.setText("");
        setImage(btn_unAutoNext, "unAutoNextIcon.png");

        /**
         * set parameter of timeProgress
         */
        timeProgress.setValue(0.0);
        timeProgress.setLayoutY(screenParameter.getScreenHeight() - 150);
        timeProgress.setLayoutX(50);
        timeProgress.setMinWidth(screenParameter.getScreenWidth() - 100);
        timeProgress.setDisable(true);
        timeProgress.setVisible(true);

        /**
         * set parameter of changeVolumne
         */
        changeVolumne.setValue(0.0);
        changeVolumne.setLayoutY(screenParameter.getScreenHeight() - 100);
        changeVolumne.setLayoutX(screenParameter.getScreenWidth() - 200);
        changeVolumne.setDisable(false);
        changeVolumne.setValue(50);
        volomne = changeVolumne.getValue();
        invoked();

        /**
         * set parameter of increaseTime
         */
        increaseTime.setLayoutY(timeProgress.getLayoutY());
        increaseTime.setLayoutX(10);
        increaseTime.setText("00:00");

        /**
         * set parameter of fullTime
         */
        fullTime.setLayoutY(timeProgress.getLayoutY());
        fullTime.setLayoutX(screenParameter.getScreenWidth() - 40);
        fullTime.setText("00:00");

        /**
         * set parameter of listView
         */
        listView.setLayoutY(30);
        listView.setMinWidth(screenParameter.getScreenWidth() * 85 / 100);
        listView.setMinHeight(screenParameter.getScreenHeight() * 60 / 100);
        listView.setBorder(null);
        makingListViewCell();

        /**
         * set parameter of nameSong
         */
        nameSong.setLayoutY(screenParameter.getScreenHeight() - 200);
        nameSong.setLayoutX(0);
        nameSong.setMinWidth(screenParameter.getScreenWidth());

        /**
         * set parameter of searchBox
         */
        searchBox.setLayoutY(0);
        searchBox.setLayoutX(listView.getLayoutX() + 50);

        /**
         * set parameter of lb_Search
         */
        lb_Search.setLayoutY(2);
        lb_Search.setLayoutX(listView.getLayoutX());
        lb_Search.setFont(new Font(15.0));
        lb_Search.setText("Search");

        /**
         * set parameter of lb_title
         */
        lb_title.setLayoutY(2);
        lb_title.setLayoutX(screenParameter.getScreenWidth() / 2 + 100);
        lb_title.setText("Các bài hát trong cùng thư mục");
        lb_title.setFont(new Font(15.0));

        /**
         * set parameter of choiceBox
         */
        initilaizeChoieBox();
        choiceBox.setOnAction(this::pick);
        choiceBox.setMinSize(50, 25);
        setPosChoiceBox(screenParameter.getScreenWidth() / 2 - choiceBox.getMinWidth() / 2,
                screenParameter.getScreenHeight() / 2 - choiceBox.getMinHeight() / 2);

        /**
         * set parameter of btn_sound
         */
        btn_sound.setLayoutY(changeVolumne.getLayoutY() - 15);
        btn_sound.setLayoutX(screenParameter.getScreenWidth() - 250);
        btn_sound.setVisible(true);
        btn_sound.setText("");
        setImage(btn_sound, "soundIcon.png");

        /**
         * set parameter of btn_mute
         */
        btn_mute.setLayoutY(changeVolumne.getLayoutY() - 15);
        btn_mute.setLayoutX(screenParameter.getScreenWidth() - 250);
        btn_mute.setText("");
        setImage(btn_mute, "muteSoundIcon.png");

        unVisables(listView, nameSong, searchBox, lb_Search, lb_title, btn_mute, btn_unShuffle, btn_unAutoNext);
    }
}
