package studio.anverso.heartratemonitor2.Model;

public class Users2 {
    private String id, id_chaleco, email, name;

    public Users2() {
    }

    public Users2(String id, String id_chaleco, String email, String name) {
        this.id = id;
        this.id_chaleco = id_chaleco;
        this.email = email;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_chaleco() {
        return id_chaleco;
    }

    public void setId_chaleco(String id_chaleco) {
        this.id_chaleco = id_chaleco;
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
}
