package www.weride.com.classes;

/**
 * Created by Francis on 4/13/17.
 */

public class UserProfile {
    private String header;
    private String profileContent;

    public UserProfile(String header, String profileContent) {
        this.header = header;
        this.profileContent = profileContent;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getProfileContent() {
        return profileContent;
    }

    public void setProfileContent(String profileContent) {
        this.profileContent = profileContent;
    }
}
