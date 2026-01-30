package edu.ithaca.dturnbull.bank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;


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

        assertFalse(BankAccount.isEmailValid("a.@b.com")); //'@'and '.' are next to eachother
        assertFalse(BankAccount.isEmailValid(".a@b.com")); //starts with '.'
        assertFalse(BankAccount.isEmailValid("a..a@b.com")); //two '.' in a row
        assertFalse(BankAccount.isEmailValid("ab.com")); //no '@' symbol
        assertFalse(BankAccount.isEmailValid("a@b@b.com")); //two '@' symbols
        assertFalse(BankAccount.isEmailValid("a")); //no '@' symbol, or domain
        assertFalse(BankAccount.isEmailValid("@b.com")); //starts with '@' symbol
        assertFalse(BankAccount.isEmailValid("a@")); //ends with '@' symbol
        assertFalse(BankAccount.isEmailValid("a@b.c")); //domain (after the '.') too short
        assertFalse(BankAccount.isEmailValid("-a@b.com")); //starts with a character that is not allowed
        //assertFalse(BankAccount.isEmailValid("a@b.aa")); // technically not a real tld
        assertFalse(BankAccount.isEmailValid("a@b.co.uk")); // multiple '.' in domain not allowed
        assertFalse(BankAccount.isEmailValid("4@4.5")); //domain (after the '.') are not letters
        assertFalse(BankAccount.isEmailValid("a@a#b.com")); //character '#' is not allowed
        assertFalse(BankAccount.isEmailValid("a@ab")); //`no '.'
        assertFalse(BankAccount.isEmailValid("a@a..com")); //two '.' in a row

        //all valid emails addresses
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