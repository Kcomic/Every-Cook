package kmitl.s8070074.bawonsak.everycook.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Food implements Parcelable {

    private String name;
    private ArrayList<String> method;
    private String detail;
    private Map<String, String> materials;
    private Map<String, String> garnish;
    private String rating;
    private String view;
    private String url;
    private Member member;
    private ArrayList<Comment> comments;

    public Food(String name, ArrayList<String> method, String detail, Map<String, String> materials, Map<String, String> garnish, String rating, String view, String url) {
        this.name = name;
        this.method = method;
        this.detail = detail;
        this.materials = materials;
        this.garnish = garnish;
        this.rating = rating;
        this.view = view;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getMethod() {
        return method;
    }

    public void setMethod(ArrayList<String> method) {
        this.method = method;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Map<String, String> getMaterials() {
        return materials;
    }

    public void setMaterials(Map<String, String> materials) {
        this.materials = materials;
    }

    public Map<String, String> getGarnish() {
        return garnish;
    }

    public void setGarnish(Map<String, String> garnish) {
        this.garnish = garnish;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComment(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeStringList(this.method);
        dest.writeString(this.detail);
        dest.writeInt(this.materials.size());
        for (Map.Entry<String, String> entry : this.materials.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
        dest.writeInt(this.garnish.size());
        for (Map.Entry<String, String> entry : this.garnish.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
        dest.writeString(this.rating);
        dest.writeString(this.view);
        dest.writeString(this.url);
        dest.writeParcelable(this.member, flags);
        dest.writeTypedList(this.comments);
    }

    protected Food(Parcel in) {
        this.name = in.readString();
        this.method = in.createStringArrayList();
        this.detail = in.readString();
        int materialsSize = in.readInt();
        this.materials = new HashMap<String, String>(materialsSize);
        for (int i = 0; i < materialsSize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.materials.put(key, value);
        }
        int garnishSize = in.readInt();
        this.garnish = new HashMap<String, String>(garnishSize);
        for (int i = 0; i < garnishSize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.garnish.put(key, value);
        }
        this.rating = in.readString();
        this.view = in.readString();
        this.url = in.readString();
        this.member = in.readParcelable(Member.class.getClassLoader());
        this.comments = in.createTypedArrayList(Comment.CREATOR);
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel source) {
            return new Food(source);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };
}
