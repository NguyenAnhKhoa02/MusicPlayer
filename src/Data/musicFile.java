package Data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class musicFile {
    public musicFile() {
        readerFile();
    }

    private void readerFile() {
        try {
            fileReader = new FileReader("../MusicPlayer/src/Data/fileSave.txt");
            bufferReader = new BufferedReader(fileReader);

            urlFile = bufferReader.readLine();
            if (urlFile == null)
                urlFile = "";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferReader.close();
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void writeFile(String url) {
        try {
            fileWriter = new FileWriter("../MusicPlayer/src/Data/fileSave.txt");
            bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.append(url);
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public boolean isUrlFile() {
        if (!urlFile.equals(""))
            return true;
        return false;
    }

    public String getUrlFile() {
        return urlFile;
    }

    private FileReader fileReader;
    private BufferedReader bufferReader;

    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;

    private String urlFile;
}
