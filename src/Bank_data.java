import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
public class Bank_data {
    Map<Human, Account> accounts = new HashMap<>();

    public void creating_new_client(String name,String lname,String pesel,String nr,String Pin){
        Human human = new Human(name,pesel,lname);
        Account account = new Account(nr,Pin,0);
        accounts.put(human,account);
    }

    public void creating_map() {
        Account kmarka = new Account("22222222222222222222222222", "2222", 2971.23);
        Human marek = new Human("Marek", "57101798239", "Świr");
        accounts.put(marek, kmarka);

        kmarka = new Account("22222222222222222222222223", "2222", 0);
        marek = new Human("Jarek", "57101798239", "Świr");
        accounts.put(marek, kmarka);
    }

    public boolean checking_client(String pesel) {
        for (Human human : accounts.keySet()) {
            if (human.getPesel().equals(pesel)) {
                return true;
            }
        }
        return false;
    }

    public boolean logging_in(String card_number, String PIN) {
        for (Account account : accounts.values()) {
            if (account.getPIN().equals(PIN) && account.getNumber().equals(card_number)) {
                return true;
            }
        }
        return false;
    }

    public boolean transfer(String sender_card_number, double amount, String receiver_card_number) {
        Account senderAccount = null;
        Account receiverAccount = null;

        for (Account account : accounts.values()) {
            if (account.getNumber().equals(sender_card_number)) {
                senderAccount = account;
            } else if (account.getNumber().equals(receiver_card_number)) {
                receiverAccount = account;
            }

            if (senderAccount != null && receiverAccount != null) {
                break;
            }
        }

        if (senderAccount == null || receiverAccount == null) {
            return false;
        }

        if (senderAccount.getMoney() < amount) {
            return false;
        }

        senderAccount.setMoney(senderAccount.getMoney() - amount);
        receiverAccount.setMoney(receiverAccount.getMoney() + amount);

        return true;
    }
    public boolean changing_pin(String oldPin, String newPin, String card) {
        for (Account account : accounts.values()) {
            if (account.getNumber().equals(card)) {
                if (account.getPIN().equals(oldPin)) {
                    account.setPIN(newPin);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }
    public boolean withdrawing_money(String card_number, double amount) {
        for (Account account : accounts.values()) {
            if (account.getNumber().equals(card_number)) {
                if (account.getMoney() >= amount) {
                    account.setMoney(account.getMoney() - amount);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }
    public void deposit(String card,double amount){
        for(Account account : accounts.values()){
            if(account.getNumber().equals(card)){
                account.setMoney(account.getMoney() + amount);
            }
        }
    }
    public double check_balance(String card_number){
        for(Account account : accounts.values()){
            if(account.getNumber().equals(card_number)){
                return account.getMoney();
            }
        }
        return 0;
    }
    public String generateUniqueAccountNumber() {
        String accountNumber;
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 26; i++) {
                sb.append(random.nextInt(10));
            }
            accountNumber = sb.toString();
        } while (accounts.containsValue(accountNumber));
        return accountNumber;
    }
    public String generateRandomPIN() {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}