package kmitl.s8070074.bawonsak.everycook.Model;

import java.util.ArrayList;

public class History {

    private String username;
    private int times;
    private ArrayList<Integer> choose;

    public History(String username, int times, ArrayList<Integer> choose) {
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

    public ArrayList<Integer> getChoose() {
        return choose;
    }

    public void setChoose(ArrayList<Integer> choose) {
        this.choose = choose;
    }
}
