package kmitl.s8070074.bawonsak.everycook.Model;

public class Food {

    private String name;
    private String method;
    private String detail;
    private int rating;
    private Comment comment;

    public Food(String name, String method, String detail, int rating, Comment comment) {
        this.name = name;
        this.method = method;
        this.detail = detail;
        this.rating = rating;
        this.comment = comment;
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
}
