package edu.ithaca.dturnbull.bank;

public class BankAccount {

    private String email;
    private double balance;

    /**
     * @throws IllegalArgumentException if email is invalid
     */
    public BankAccount(String email, double startingBalance){
        if (isEmailValid(email)){
            this.email = email;
            this.balance = startingBalance;
        }
        else {
            throw new IllegalArgumentException("Email address: " + email + " is invalid, cannot create account");
        }
    }

    public double getBalance(){
        return balance;
    }

    public String getEmail(){
        return email;
    }

    /**
     * @post reduces the balance by amount if amount is non-negative and smaller than balance
     */
    public void withdraw (double amount) throws InsufficientFundsException{
        if (amount <= balance){
            balance -= amount;
        }
        else {
            throw new InsufficientFundsException("Not enough money");
        }
    }


public static boolean isEmailValid(String email) {
    if (email == null || email.isEmpty()) return false;

    if (email.contains("..")) return false;

    int at = email.indexOf('@');
    if (at <= 0 || at != email.lastIndexOf('@')) return false;

    String local = email.substring(0, at);
    String domain = email.substring(at + 1);

    if (domain.isEmpty()) return false;



    char first = local.charAt(0);
    if (!Character.isLetter(first)) return false;
    
    for (char c : local.toCharArray()) {
        if (!Character.isLetterOrDigit(c) && "+._-".indexOf(c) == -1) {
            return false;
        }
    }

    if (local.startsWith(".") || local.endsWith(".")) return false;

    if (email.contains(".@")) return false;

 
    

    if (!domain.contains(".")) return false;
    if (domain.startsWith(".") || domain.endsWith(".")) return false;

    String[] labels = domain.split("\\.");
    for (String label : labels) {
        if (label.isEmpty()) return false;

        for (char c : label.toCharArray()) {
            if (!Character.isLetter(c)) return false;
        }
    }

  
    String tld = labels[labels.length - 1];
    if (tld.length() < 2) return false;

    return true;
}
}