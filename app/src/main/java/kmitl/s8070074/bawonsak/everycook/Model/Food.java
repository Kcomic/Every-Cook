package kmitl.s8070074.bawonsak.everycook.Model;

import java.util.ArrayList;

public class Food {

    private String name;
    private String method;
    private String detail;
    private ArrayList<String> materails;
    private int rating;
    private Comment comment;

    public Food(String name, String method, String detail, int rating, Comment comment, ArrayList<String> materails) {
        this.name = name;
        this.method = method;
        this.detail = detail;
        this.rating = rating;
        this.comment = comment;
        this.materails = materails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public ArrayList<String> getMaterails() {
        return materails;
    }

    public void setMaterails(ArrayList<String> materails) {
        this.materails = materails;
    }
}
