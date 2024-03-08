package Business.Java;

import java.io.File;

public class fileMusic {
    public fileMusic(File file) {
        this.file = file;
        parentFile = file.getParent() + "\\";
        nameMusic = file.getName();
    }

    public String getParentFile() {
        return parentFile;
    }

    public String getNameMusic() {
        return nameMusic;
    }

    private File file;
    private String parentFile;
    private String nameMusic;
}
