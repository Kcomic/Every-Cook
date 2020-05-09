package kmitl.s8070074.bawonsak.everycook.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {

    private String message;
    private Member member;
    private String date;
    private String time;

    public Comment(String message, String date, String time) {
        this.message = message;
        this.date = date;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
        dest.writeParcelable(this.member, flags);
        dest.writeString(this.date);
        dest.writeString(this.time);
    }

    protected Comment(Parcel in) {
        this.message = in.readString();
        this.member = in.readParcelable(Member.class.getClassLoader());
        this.date = in.readString();
        this.time = in.readString();
    }

    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
