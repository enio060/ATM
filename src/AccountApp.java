import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;

public class AccountApp extends Frame implements ActionListener {
    private Bank_data bank;
    private Button createAccountButton;
    private Button loginButton;
    private Panel mainPanel;
    private Panel createAccountPanel;
    private Panel loginPanel;
    private Panel Successful_creating;
    private Button submitButton;
    private Panel Account_option_panel;
    private Button backbutton;
    private Panel Transferpanel;

    public AccountApp() {
        if (bank == null) {
            bank = new Bank_data();
            bank.creating_map();
        }
        setLayout(new BorderLayout());


        createAccountButton = new Button("Create Account");
        createAccountButton.addActionListener(this);

        loginButton = new Button("Login");
        loginButton.addActionListener(this);

        mainPanel = new Panel();
        mainPanel.setLayout(new GridLayout(2, 1));
        mainPanel.add(createAccountButton);
        mainPanel.add(loginButton);
        add(mainPanel, BorderLayout.CENTER);

        setTitle("Bank");
        setSize(300, 200);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createAccountButton) {
            showCreateAccountPanel();
        } else if (e.getSource() == loginButton) {
            showLoginPanel();
        }
    }

    private void showCreateAccountPanel() {
        createAccountPanel = new Panel();

        createAccountPanel.setLayout(new GridLayout(5, 2, 5, 5));

        JLabel nameLabel = new JLabel("First Name");
        JTextField nameField = new JTextField(15);
        JLabel lastNameLabel = new JLabel("Last Name");
        JTextField lastNameField = new JTextField(15);
        JLabel peselLabel = new JLabel("PESEL");
        JTextField peselField = new JTextField(15);
        submitButton = new Button("Submit");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String lastName = lastNameField.getText();
                String pesel = peselField.getText();

                if (!checking_if_not_empty(name) || !checking_if_not_empty(lastName)) {
                    JOptionPane.showMessageDialog(AccountApp.this, "PLEASE FILL IN ALL FIELDS!", "MISTAKE", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean check_pesel = bank.checking_client(pesel);
                if (check_pesel) {
                    JOptionPane.showMessageDialog(AccountApp.this, "USER ALREADY EXISTS!", "MISTAKE", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!checking_id(pesel)) {
                    JOptionPane.showMessageDialog(AccountApp.this, "INVALID PESEL!", "MISTAKE", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Successful_creating_account(name , lastName, pesel);
            }
        });
        backbutton = new Button("Back");
        backbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showOptionsPanel();
            }
        });
        createAccountPanel.add(nameLabel);
        createAccountPanel.add(nameField);
        createAccountPanel.add(lastNameLabel);
        createAccountPanel.add(lastNameField);
        createAccountPanel.add(peselLabel);
        createAccountPanel.add(peselField);
        createAccountPanel.add(submitButton);
        createAccountPanel.add(backbutton);
        removeAll();
        add(createAccountPanel, BorderLayout.CENTER);
        validate();
    }

    private void showLoginPanel() {
        loginPanel = new Panel();
        loginPanel.setLayout(new GridLayout(5, 2, 5, 5));

        JLabel cardLabel = new JLabel("Card Number:");
        JTextField cardField = new JTextField(20);
        JLabel pinLabel = new JLabel("PIN:");
        JTextField pinField = new JTextField(20);
        submitButton = new Button("Submit");
        backbutton = new Button("Back");
        backbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showOptionsPanel();
            }
        });
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String card = cardField.getText();
                String pin = pinField.getText();
                if (bank.logging_in(card, pin)) {
                    Logging_panel(card);
                    remove(loginPanel);
                    validate();
                } else {
                    JOptionPane.showMessageDialog(AccountApp.this, "WRONG CARD_NUMBER/PIN!", "MISTAKE", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        });

        loginPanel.add(cardLabel);
        loginPanel.add(cardField);
        loginPanel.add(pinLabel);
        loginPanel.add(pinField);
        loginPanel.add(submitButton);
        loginPanel.add(backbutton);

        removeAll();
        add(loginPanel, BorderLayout.CENTER);
        validate();
    }

    public static boolean checking_id(String ID) {
        if (ID.length() != 11) {
            return false;
        }
        try {
            Long.parseLong(ID); //Checking if ID can be converted into int
        } catch (NumberFormatException e) {
            return false;
        }

        int Control_Number = 0;
        int[] weights = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3, 1};
        for (int i = 0; i < 10; i++) {
            Control_Number += weights[i] * Character.getNumericValue(ID.charAt(i));
        }
        Control_Number = (10 - (Control_Number % 10)) % 10;
        return Control_Number == Character.getNumericValue(ID.charAt(10));
    }

    private void Successful_creating_account(String name ,String lname , String pesel) {
        Successful_creating = new Panel();
        Successful_creating.setLayout(new GridLayout(3, 2, 2, 5));
        String nr = bank.generateUniqueAccountNumber();
        String pin = bank.generateRandomPIN();
        bank.creating_new_client(name,lname,pesel,nr,pin);
        JLabel account_nr = new JLabel("Your account number: " + nr);
        JLabel password = new JLabel("Generated PIN: " + pin);
        submitButton = new Button("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(nr);
                Logging_panel(nr);
            }
        });

        Successful_creating.add(account_nr);
        Successful_creating.add(password);
        Successful_creating.add(submitButton);

        removeAll();
        add(Successful_creating, BorderLayout.CENTER);
        validate();
    }

    public static boolean checking_if_not_empty(String data) {
        String znaki = "^[a-zA-Z ]+$";
        return Pattern.matches(znaki, data);
    }

    public void Logging_panel(String user_card_number) {
        removeAll();
        setLayout(new BorderLayout());
        Account_option_panel = new Panel();
        Account_option_panel.setLayout(new GridLayout(6, 2, 5, 5));

        JLabel account_cashLabel = new JLabel("Checking account balance");
        JButton accountCashButton = new JButton("Check Balance");

        JLabel depositLabel = new JLabel("Deposit");
        JButton depositButton = new JButton("Deposit");
        accountCashButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkBalancePanel(user_card_number);
            }
        });
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                depositPanel(user_card_number);
            }
        });

        JLabel withdrawalsLabel = new JLabel("Withdrawals");
        JButton withdrawalsButton = new JButton("Withdraw");
        withdrawalsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                withdrawPanel(user_card_number);
            }
        });

        JLabel changingpinLabel = new JLabel("Change PIN");
        JButton changePinButton = new JButton("Change PIN");
        changePinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showChangingPinPanel(user_card_number);
            }
        });

        JLabel transferLabel = new JLabel("Transfer");
        JButton transferButton = new JButton("Transfer");

        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                show_transfer_panel(user_card_number);
            }
        });

        JLabel loggingoutLabel = new JLabel("Log out");
        JButton logoutButton = new JButton("Log out");

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showOptionsPanel();
            }
        });

        Account_option_panel.add(account_cashLabel);
        Account_option_panel.add(accountCashButton);
        Account_option_panel.add(depositLabel);
        Account_option_panel.add(depositButton);
        Account_option_panel.add(withdrawalsLabel);
        Account_option_panel.add(withdrawalsButton);
        Account_option_panel.add(changingpinLabel);
        Account_option_panel.add(changePinButton);
        Account_option_panel.add(transferLabel);
        Account_option_panel.add(transferButton);
        Account_option_panel.add(loggingoutLabel);
        Account_option_panel.add(logoutButton);

        add(Account_option_panel, BorderLayout.CENTER);
        validate();
    }

    private void showOptionsPanel() {
        removeAll();
        setLayout(new BorderLayout());

        mainPanel = new Panel();
        mainPanel.setLayout(new GridLayout(2, 1));

        createAccountButton = new Button("Create Account");
        createAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showCreateAccountPanel();
            }
        });

        loginButton = new Button("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoginPanel();
            }
        });

        mainPanel.add(createAccountButton);
        mainPanel.add(loginButton);

        add(mainPanel, BorderLayout.CENTER);
        validate();
    }
    private void show_transfer_panel(String sender_number){
        removeAll();
        setLayout(new BorderLayout());

        Transferpanel = new Panel();
        Transferpanel.setLayout(new GridLayout(4, 2, 5, 5));

        JLabel MoneyLabel = new JLabel("How much:");
        JTextField moneyField = new JTextField(20);
        JLabel numberLabel = new JLabel("Card number of receiver:");
        JTextField numberField = new JTextField(20);

        submitButton = new Button("Submit");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String amountString = moneyField.getText();
                double amount = Double.parseDouble(amountString);
                String reciver = numberField.getText();
                if(bank.transfer(sender_number,amount,reciver)){
                    Logging_panel(sender_number);
                }else {
                    JOptionPane.showMessageDialog(AccountApp.this, "CHECK IF CARD NUMBER IS VALID OR CHECK IF YOU HAVE ENOUGH MONEY!", "MISTAKE", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        });

        backbutton = new Button("Back");
        backbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Logging_panel(sender_number);
            }
        });

        Transferpanel.add(MoneyLabel);
        Transferpanel.add(moneyField);
        Transferpanel.add(numberLabel);
        Transferpanel.add(numberField);
        Transferpanel.add(submitButton);
        Transferpanel.add(backbutton);

        add(Transferpanel, BorderLayout.CENTER);
        validate();
    }
    private void showChangingPinPanel(String cardNumber) {
        removeAll();
        setLayout(new BorderLayout());

        Panel changingPinPanel = new Panel();
        changingPinPanel.setLayout(new GridLayout(3, 2, 5, 5));

        JLabel currentPinLabel = new JLabel("Current PIN:");
        JTextField currentPinField = new JTextField(20);
        JLabel newPinLabel = new JLabel("New PIN:");
        JTextField newPinField = new JTextField(20);

        Button submitButton = new Button("Submit");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String currentPin = currentPinField.getText();
                String newPin = newPinField.getText();
                if (bank.changing_pin(currentPin,newPin,cardNumber)){
                    Logging_panel(cardNumber);
                }else{
                    JOptionPane.showMessageDialog(AccountApp.this, "WRONG PIN", "MISTAKE", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        });

        Button backButton = new Button("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Logging_panel(cardNumber);
            }
        });

        changingPinPanel.add(currentPinLabel);
        changingPinPanel.add(currentPinField);
        changingPinPanel.add(newPinLabel);
        changingPinPanel.add(newPinField);
        changingPinPanel.add(submitButton);
        changingPinPanel.add(backButton);

        add(changingPinPanel, BorderLayout.CENTER);
        validate();
    }
    private void withdrawPanel(String card_number) {
        removeAll();
        setLayout(new BorderLayout());

        Panel withdrawPanel = new Panel();
        withdrawPanel.setLayout(new GridLayout(3, 2, 5, 5));

        JLabel HowMuchLabel = new JLabel("How much:");
        JTextField HowmuchField = new JTextField(20);
        Button submitButton = new Button("Submit");
        Button backButton = new Button("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Logging_panel(card_number);
            }
        });
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String amountString = HowmuchField.getText();
                double amount = Double.parseDouble(amountString);
                if (bank.withdrawing_money(card_number, amount)) {
                    Logging_panel(card_number);
                } else {
                    JOptionPane.showMessageDialog(AccountApp.this, "NOT ENOUGH MONEY", "MISTAKE", JOptionPane.ERROR_MESSAGE);
                    return;
                }

            }
        });

        withdrawPanel.add(HowMuchLabel);
        withdrawPanel.add(HowmuchField);
        withdrawPanel.add(submitButton);
        withdrawPanel.add(backButton);

        add(withdrawPanel, BorderLayout.CENTER);
        validate();
    }
    private void depositPanel(String card_number) {
        removeAll();
        setLayout(new BorderLayout());

        Panel depositPanel = new Panel();
        depositPanel.setLayout(new GridLayout(3, 2, 5, 5));

        JLabel HowMuchLabel = new JLabel("How much:");
        JTextField HowmuchField = new JTextField(20);
        Button submitButton = new Button("Submit");
        Button backButton = new Button("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Logging_panel(card_number);
            }
        });
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String amountString = HowmuchField.getText();
                double amount = Double.parseDouble(amountString);
                bank.deposit(card_number, amount);
                Logging_panel(card_number);
            }
        });

        depositPanel.add(HowMuchLabel);
        depositPanel.add(HowmuchField);
        depositPanel.add(submitButton);
        depositPanel.add(backButton);

        add(depositPanel, BorderLayout.CENTER);
        validate();
    }
    private void checkBalancePanel(String card_number) {
        removeAll();
        setLayout(new BorderLayout());

        Panel balancePanel = new Panel();
        balancePanel.setLayout(new GridLayout(2, 1));

        JLabel balanceLabel = new JLabel("Your current balance is: $" + bank.check_balance(card_number));
        Button backButton = new Button("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Logging_panel(card_number);
            }
        });

        balancePanel.add(balanceLabel);
        balancePanel.add(backButton);

        add(balancePanel, BorderLayout.CENTER);
        validate();
    }

}

