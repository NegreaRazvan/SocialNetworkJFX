package layers.domain;

import java.util.Objects;

public class User extends Entity<Long>{
    private String firstName;
    private String lastName;
    private String password;
    private String username;
    private Boolean isAdmin;
    private Integer numberOfNotifications;

    public User(String firstName, String lastName,String password, String username, Boolean isAdmin, Integer numberOfNotifications) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.username = username;
        this.isAdmin = isAdmin;
        this.numberOfNotifications = numberOfNotifications;
    }

    /**
     * getter for the first name of the user
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * setter for the first name of the user
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * getter for the last name of the user
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * setter for the last name of the user
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) { this.username = username; }
    public Boolean getIsAdmin() { return isAdmin; }
    public void setIsAdmin(Boolean isAdmin) { this.isAdmin = isAdmin; }
    public Integer getNumberOfNotifications() { return numberOfNotifications; }
    public void setNumberOfNotifications(Integer numberOfNotifications) { this.numberOfNotifications = numberOfNotifications; }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName());
    }
}