package APIAutotests;

public class PojoClass {
    private String login;
    private String pass;

    public PojoClass(){

    }

    public PojoClass(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }



    public String getLogin() {

        return login;
    }

    public String getPass() {

        return pass;
    }


}