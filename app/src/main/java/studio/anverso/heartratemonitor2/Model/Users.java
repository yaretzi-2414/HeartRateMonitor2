package studio.anverso.heartratemonitor2.Model;

public class Users {
    private String id, email, name, password, tipous, gender, age, name_contact, phone_contact;

    public Users() {

    }

    public Users(String id, String email, String name, String password, String tipous, String gender, String age, String name_contact, String phone_contact) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.tipous = tipous;
        this.gender = gender;
        this.age = age;
        this.name_contact = name_contact;
        this.phone_contact = phone_contact;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTipous() {
        return tipous;
    }

    public void setTipous(String tipous) {
        this.tipous = tipous;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getName_contact() {
        return name_contact;
    }

    public void setName_contact(String name_contact) {
        this.name_contact = name_contact;
    }

    public String getPhone_contact() {
        return phone_contact;
    }

    public void setPhone_contact(String phone_contact) {
        this.phone_contact = phone_contact;
    }
}
