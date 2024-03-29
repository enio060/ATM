public class Account {
    public Account(String number, String PIN, double money) {
        this.number = number;
        this.PIN = PIN;
        this.money = money;
    }

    String number ;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPIN() {
        return PIN;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    String PIN;
    double money;

}
