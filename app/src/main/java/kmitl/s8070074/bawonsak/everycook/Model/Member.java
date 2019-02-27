package kmitl.s8070074.bawonsak.everycook.Model;

public class Member {

    private String username;
    private String phone;
    private String name;

    public Member(String username, String phone, String name) {
        this.username = username;
        this.phone = phone;
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
