package Business.Java;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import Data.musicFile;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class manageFile {
    manageFile() {
        musicFile = new musicFile();
    }

    public void initializationListFile() {
        arrListfileMusic = new ArrayList<>();
        File file = new File(musicFile.getUrlFile());
        filterLisfile(file);
        convertListFileToFileMusic(listFile);
        parentFile = musicFile.getUrlFile();
    }

    private void filterLisfile(File f) {
        FileFilter fileFilter = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith("mp3");
            }

        };

        listFile = f.listFiles(fileFilter);
    }

    /*
     * return folder path from directoryChooser
     * * * * * * * * * * * * * * * * * *
     * show directoryChooser.dialog()
     * fileFiler and return the listFile
     * * * * * * * * * * * * * * * * * *
     */
    public void openSelectFolder() {
        arrListfileMusic = new ArrayList<>();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(null);
        filterLisfile(file);
        convertListFileToFileMusic(listFile);
        parentFile = arrListfileMusic.get(0).getParentFile();
    }

    /*
     * return folder path from fileChooser
     * * * * * * * * * * * * * * * * * *
     * show fileChooser.showOpenDialog()
     * fileFiler and return the listFile
     * * * * * * * * * * * * * * * * * *
     */
    public void openSelectFile() {
        arrListfileMusic = new ArrayList<>();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new ExtensionFilter("Music file", "*.mp3"));
        File file = fileChooser.showOpenDialog(null);
        filterLisfile(file.getParentFile());
        fileChoose = file.getName();
        convertListFileToFileMusic(listFile);
        parentFile = arrListfileMusic.get(0).getParentFile();
    }

    /**
     * 
     * @return the fileChoose from openSelectFile()
     */
    public String getNameFileChooser() {
        return fileChoose;
    }

    /**
     * 
     * @return the parentFile
     */
    public String getParentFile() {
        return parentFile;
    }

    /**
     * 
     * @return the listFileName from ArrayList<fileMusic>
     */
    public String[] getListName() {
        listFileName = new String[arrListfileMusic.size()];
        convertArrToList();
        return listFileName;
    }

    /**
     * convert ArrayList<fileMusic> to File[] listFileName
     */
    private void convertArrToList() {
        for (int i = 0; i < arrListfileMusic.size(); i++) {
            listFileName[i] = arrListfileMusic.get(i).getNameMusic();
        }
    }

    /**
     * 
     * @param listFile
     *                 return listFile to ArrayList<fileMusic>
     */
    private void convertListFileToFileMusic(File[] listFile) {
        for (File f : listFile) {
            arrListfileMusic.add(new fileMusic(f));
        }
    }

    public boolean isUrlFile() {
        return musicFile.isUrlFile();
    }

    public void writeFile(String urlFile) {
        musicFile.writeFile(urlFile);
    }

    private ArrayList<fileMusic> arrListfileMusic;
    private String[] listFileName;
    private String parentFile;
    private File[] listFile;
    private String fileChoose;
    private musicFile musicFile;
}
