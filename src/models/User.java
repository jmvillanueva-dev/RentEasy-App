package models;


public class User {
    private int id;
    private String username;
    private String lastname;
    private String email;
    private String password;
    private boolean acceptedPrivacyPolicy;
    private boolean acceptedTermsAndConditions;

    // Constructor
    public User(String username, String lastname, String email, String password, boolean acceptedPrivacyPolicy, boolean acceptedTermsAndConditions) {
        this.username = username;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.acceptedPrivacyPolicy = acceptedPrivacyPolicy;
        this.acceptedTermsAndConditions = acceptedTermsAndConditions;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isAcceptedPrivacyPolicy() { return acceptedPrivacyPolicy; }
    public void setAcceptedPrivacyPolicy(boolean acceptedPrivacyPolicy) { this.acceptedPrivacyPolicy = acceptedPrivacyPolicy; }

    public boolean isAcceptedTermsAndConditions() { return acceptedTermsAndConditions; }
    public void setAcceptedTermsAndConditions(boolean acceptedTermsAndConditions) { this.acceptedTermsAndConditions = acceptedTermsAndConditions; }
}
