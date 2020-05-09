package kmitl.s8070074.bawonsak.everycook;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class DataHelper implements Parcelable {

    private ArrayList<ArrayList<Double>> cosim;

    public ArrayList<ArrayList<Double>> getCosim() {
        return cosim;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.cosim);
    }

    public DataHelper(ArrayList<ArrayList<Double>> cosim) {
        this.cosim = cosim;
    }

    protected DataHelper(Parcel in) {
        this.cosim = new ArrayList<ArrayList<Double>>();
        in.readList(this.cosim, ArrayList.class.getClassLoader());
    }

    public static final Parcelable.Creator<DataHelper> CREATOR = new Parcelable.Creator<DataHelper>() {
        @Override
        public DataHelper createFromParcel(Parcel source) {
            return new DataHelper(source);
        }

        @Override
        public DataHelper[] newArray(int size) {
            return new DataHelper[size];
        }
    };
}
