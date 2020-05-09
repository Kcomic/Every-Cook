package kmitl.s8070074.bawonsak.everycook.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class History implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeInt(this.times);
        dest.writeList(this.choose);
    }

    protected History(Parcel in) {
        this.username = in.readString();
        this.times = in.readInt();
        this.choose = new ArrayList<Integer>();
        in.readList(this.choose, Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<History> CREATOR = new Parcelable.Creator<History>() {
        @Override
        public History createFromParcel(Parcel source) {
            return new History(source);
        }

        @Override
        public History[] newArray(int size) {
            return new History[size];
        }
    };
}
