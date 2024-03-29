public class Human {
    public Human(String name, String pesel, String last_name) {
        Name = name;
        Pesel = pesel;
        Last_name = last_name;
    }

    public String getName() {
        return Name;
    }

    public String getPesel() {
        return Pesel;
    }

    public String getLast_name() {
        return Last_name;
    }

    String Name ;
    String Pesel;

    public void setName(String name) {
        Name = name;
    }

    public void setPesel(String pesel) {
        Pesel = pesel;
    }

    public void setLast_name(String last_name) {
        Last_name = last_name;
    }

    String Last_name;
}
