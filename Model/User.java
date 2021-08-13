package Model;

public class User {
    private final int id;
    private final String name;
    private final String password;

    public User(int id, String username, String password){
        this.id=id;
        this.name=username;
        this.password=password;

    }

    public int getId() {
        return id;
    }
    public String getName(){
        return name;
    }
    public String getPassword(){return password;}
}
