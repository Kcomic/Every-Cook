package kmitl.s8070074.bawonsak.everycook.Model;

import java.util.ArrayList;

public class History {

    private String username;
    private int times;
    private ArrayList<String> choose;

    public History(String username, int times, ArrayList<String> choose) {
        this.username = username;
        this.times = times;
        this.choose = choose;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public ArrayList<String> getChoose() {
        return choose;
    }

    public void setChoose(ArrayList<String> choose) {
        this.choose = choose;
    }
}
