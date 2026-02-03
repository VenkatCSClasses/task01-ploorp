package edu.ithaca.dturnbull.bank;

public class BankAccount {

    private String email;
    private double balance;

    /**
     * @throws IllegalArgumentException if email is invalid or startingBalance is invalid
     */
    public BankAccount(String email, double startingBalance){
        if (!isAmountValid(startingBalance)) {
            throw new IllegalArgumentException("Invalid starting balance: " + startingBalance);
        }
        if (!isEmailValid(email)){
            throw new IllegalArgumentException("Email address: " + email + " is invalid, cannot create account");
        }
        
        this.email = email;
        this.balance = startingBalance;
    }

    public double getBalance(){
        return balance;
    }

    public String getEmail(){
        return email;
    }

    /**
     * @post reduces the balance by amount if amount is non-negative and smaller than balance
     * @throws IllegalArgumentException if amount is negative
     * @throws InsufficientFundsException if amount is greater than balance
     */
    public void withdraw(double amount) throws InsufficientFundsException {
        if (!isAmountValid(amount)) {
            throw new IllegalArgumentException("Invalid amount: " + amount);
        }
        if (amount <= balance){
            balance -= amount;
        }
        else {
            throw new InsufficientFundsException("Not enough money");
        }
    }

    /**
     * @post increases the balance by amount if amount is non-negative
     * @throws IllegalArgumentException if amount is negative
     */
    public void deposit(double amount) throws IllegalArgumentException {
        if(!isAmountValid(amount)) {
            throw new IllegalArgumentException("Invalid amount: " + amount);
        }
        balance += amount;
    }

    /**
     * @post transfers amount from this account to destination account if amount is non-negative and smaller than balance
     * @throws IllegalArgumentException if amount is negative
     * @throws InsufficientFundsException if amount is greater than balance
     */
    public void transfer(BankAccount destination, double amount) throws InsufficientFundsException {
        if(!isAmountValid(amount)) {
            throw new IllegalArgumentException("Invalid amount: " + amount);
        }
        if (amount <= balance){
            this.withdraw(amount);
            destination.deposit(amount);
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

    if (local.length() > 64) return false;

    if (local.startsWith(".") || local.endsWith(".")) return false;

    if (local.startsWith("-") || local.endsWith("-")) return false;

    String allowedLocal = "+._-!%#&*";

    for (char c : local.toCharArray()) {
        if (!Character.isLetterOrDigit(c) && allowedLocal.indexOf(c) == -1) {
            return false;
        }
    }

    if (!domain.contains(".")) return false;

    if (domain.startsWith(".") || domain.endsWith(".")) return false;

    if (domain.length() > 255) return false;

    String[] labels = domain.split("\\.");
    for (String label : labels) {
        if (label.isEmpty()) return false;

        for (char c : label.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != '-') return false;
        }
        if (label.startsWith("-") || label.endsWith("-")) return false;
    }

    String tld = labels[labels.length - 1];

    if (tld.length() < 2 || tld.length() > 13) return false;

    for (char c : tld.toCharArray()) {
        if (!Character.isLetter(c)) return false;
    }

    return true;
}


/*
 * @return true if amount is non-negative, and fewer than 2 decimal places, false otherwise
*/
public static boolean isAmountValid(double amount) {
    return amount >= 0 && Math.round(amount * 100) == amount * 100;
}

}