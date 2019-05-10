package kmitl.s8070074.bawonsak.everycook.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Member implements Parcelable {

    private String username;
    private String nickname;
    private String rating;
    private String fullname;
    private String url;

    public Member(String username, String nickname, String rating, String fullname, String url) {
        this.username = username;
        this.nickname = nickname;
        this.rating = rating;
        this.fullname = fullname;
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    protected Member(Parcel in) {
        username = in.readString();
        rating = in.readString();
        fullname = in.readString();
        nickname = in.readString();
        url = in.readString();
    }

    public static final Creator<Member> CREATOR = new Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel in) {
            return new Member(in);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(rating);
        dest.writeString(fullname);
        dest.writeString(nickname);
        dest.writeString(url);
    }
}
