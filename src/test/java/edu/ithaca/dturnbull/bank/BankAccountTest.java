package edu.ithaca.dturnbull.bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class BankAccountTest {

    @Test
    void getBalanceTest() {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);

        assertEquals(200, bankAccount.getBalance(), 0.001);
    }

    @Test
    void withdrawTest() throws InsufficientFundsException{
        BankAccount bankAccount = new BankAccount("a@b.com", 200);
        bankAccount.withdraw(100);

        assertEquals(100, bankAccount.getBalance(), 0.001);
        assertThrows(InsufficientFundsException.class, () -> bankAccount.withdraw(300));
    }

    @Test
    void isEmailValidTest(){
        assertTrue(BankAccount.isEmailValid( "a@b.com"));   // valid email address
        assertFalse( BankAccount.isEmailValid(""));         // empty string

        assertFalse(BankAccount.isEmailValid("a.@b.com"));
        assertFalse(BankAccount.isEmailValid(".a@b.com"));
        assertFalse(BankAccount.isEmailValid("a..a@b.com"));
        assertFalse(BankAccount.isEmailValid("ab.com"));
        assertFalse(BankAccount.isEmailValid("a@b@b.com"));
        assertFalse(BankAccount.isEmailValid("a"));
        assertFalse(BankAccount.isEmailValid("@b.com"));
        assertFalse(BankAccount.isEmailValid("a@"));
        assertFalse(BankAccount.isEmailValid("a@b.c"));
        assertFalse(BankAccount.isEmailValid("-a@b.com"));
        //assertFalse(BankAccount.isEmailValid("a@b.aa")); // technically not a real tld
        assertFalse(BankAccount.isEmailValid("a@b.co.uk"));
        assertFalse(BankAccount.isEmailValid("4@4.5"));
        assertFalse(BankAccount.isEmailValid("a@a#b.com"));
        assertFalse(BankAccount.isEmailValid("a@ab"));
        assertFalse(BankAccount.isEmailValid("a@a..com"));

        assertTrue(BankAccount.isEmailValid("a@b.co.uk"));
        assertTrue(BankAccount.isEmailValid("a@a.b.com"));
        assertTrue(BankAccount.isEmailValid("a.a@b.com"));
        assertTrue(BankAccount.isEmailValid("4@b.com"));
        //assertTrue(BankAccount.isEmailValid("a-@b.com")); // valid according to wikipedia
        assertTrue(BankAccount.isEmailValid("a+b@b.com"));
        assertTrue(BankAccount.isEmailValid("A@b.com"));
    }

    @Test
    void constructorTest() {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);

        assertEquals("a@b.com", bankAccount.getEmail());
        assertEquals(200, bankAccount.getBalance(), 0.001);
        //check for exception thrown correctly
        assertThrows(IllegalArgumentException.class, ()-> new BankAccount("", 100));
    }

}