package Business.Java;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class randomMusic {
    private ArrayList<String> nameMusic;
    private ArrayList<String> arr_nameGet = new ArrayList<>();
    private ArrayList<Integer> arr_numGet = new ArrayList<>();
    private String[] list;
    private Random random;
    private String nameMusicChoose;
    private int index_choose;

    randomMusic(String name, ArrayList<String> nameMusic) {
        this.nameMusic = new ArrayList<>();
        random = new Random();
        this.nameMusic = nameMusic;
        index_choose = -1;
        if (name != "") {
            nameMusicChoose = name;
            convertNameMusicToIndex();
        }
    }

    private void convertNameMusicToIndex() {
        for (int i = 0; i < nameMusic.size(); i++) {
            if (nameMusicChoose.toLowerCase().equals(nameMusic.get(i).toLowerCase())) {
                index_choose = i;
                break;
            }

        }
    }

    private boolean isNotSame(int numrand) {
        for (int num : arr_numGet) {
            if (numrand == num || numrand == index_choose) {
                return false;
            }
        }
        return true;
    }

    public void randdom() {
        int num_rand = 0;
        for (int i = 0; i < nameMusic.size(); i++) {
            if (index_choose != -1) {
                arr_numGet.add(index_choose);
                arr_nameGet.add(nameMusic.get(index_choose));
                index_choose = -1;
                continue;
            }

            num_rand = random.nextInt(nameMusic.size());
            if (isNotSame(num_rand)) {
                arr_numGet.add(num_rand);
                arr_nameGet.add(nameMusic.get(num_rand));
            } else {
                i -= 1;
            }
        }
    }

    private void convertArrayToList(ArrayList<String> arr) {
        list = new String[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            list[i] = arr.get(i);
        }
    }

    public ArrayList<Integer> getListNumRan() {
        return arr_numGet;
    }

    public String[] getListNameRan() {
        convertArrayToList(arr_nameGet);
        return list;
    }
}
