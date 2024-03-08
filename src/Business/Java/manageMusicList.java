package Business.Java;

public class manageMusicList {
    private String[] listOriginal;
    private String[] recentList;

    public manageMusicList(String[] lisOriginal) {
        this.listOriginal = lisOriginal;
    }

    public void setRecentList(String[] recentList) {
        this.recentList = recentList;
    }

    public int getIndexMusicFromRecentList(String name) {
        int index = -1;

        if (name.equals(""))
            return index;

        for (String n : recentList) {
            index += 1;
            if (n.toLowerCase().equals(name.toLowerCase())) {
                break;
            }
        }

        return index;
    }

    public int getIndexMusicFromListOriginal(String name) {
        int index = -1;

        if (name.equals(""))
            return index;

        for (String n : listOriginal) {
            index += 1;
            if (n.toLowerCase().equals(name.toLowerCase())) {
                break;
            }
        }

        return index;
    }

    public String[] getOriginalMusic() {
        return listOriginal;
    }
}
