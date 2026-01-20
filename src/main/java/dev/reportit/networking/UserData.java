package dev.reportit.networking;

public class UserData {
    public int id;
    public String profilePic;
    public String username;
    public String email;
    public String createdAt;

    public UserData() {}

    @Override
    public String toString() {
        return String.format("""
                {
                    "id": "%d",
                    "profilePic": "%s",
                    "username": "%s",
                    "email": "%s",
                    "createdAt": "%s"
                }
                """, id, profilePic, username, email, createdAt);
    }
}
