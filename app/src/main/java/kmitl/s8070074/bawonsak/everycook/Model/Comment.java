package kmitl.s8070074.bawonsak.everycook.Model;

public class Comment {

    private String message;
    private String username;
    private String like;
    private String dislike;

    public Comment(String message, String username, String like, String dislike) {
        this.message = message;
        this.username = username;
        this.like = like;
        this.dislike = dislike;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getDislike() {
        return dislike;
    }

    public void setDislike(String dislike) {
        this.dislike = dislike;
    }
}
