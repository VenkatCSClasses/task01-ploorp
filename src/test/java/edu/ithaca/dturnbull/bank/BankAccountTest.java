package edu.ithaca.dturnbull.bank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;


class BankAccountTest {

    @Test
    void getBalanceTest() throws InsufficientFundsException {
        BankAccount acc = new BankAccount("a@b.com", 200);

        // new account starting balance check
        assertEquals(200, acc.getBalance(), 0.001);

        // small withdraw
        acc.withdraw(0.01);
        assertEquals(200 - 0.01, acc.getBalance(), 0.001);

        // big withdraw leaving small amount
        acc.withdraw(200 - 0.02);
        assertEquals(0.01, acc.getBalance(), 0.001);

        // empty
        acc.withdraw(0.01);
        assertEquals(0, acc.getBalance(), 0.001);
    }

    @Test
    void withdrawTest() throws InsufficientFundsException{
        BankAccount acc = new BankAccount("a@b.com", 200);

        // withdraw money
        acc.withdraw(0.01); // smallest possible withdraw
        acc.withdraw(100); // normal withdraw
        assertEquals(100 - 0.01, acc.getBalance(), 0.001); // check balance
        acc.withdraw(99.98); // second withdraw
        acc.withdraw(0); // make sure zero works
        assertEquals(0.01, acc.getBalance(), 0.001); // check balance

        // withdraw too much money
        assertThrows(InsufficientFundsException.class, () -> acc.withdraw(0.02)); // just over balance
        assertThrows(InsufficientFundsException.class, () -> acc.withdraw(1)); // normal over withdraw
        assertThrows(InsufficientFundsException.class, () -> acc.withdraw(1000000.0)); // really big withdraw

        // negative withdraw
        assertThrows(IllegalArgumentException.class, () -> acc.withdraw(-0.01)); // small negative withdraw
        assertThrows(IllegalArgumentException.class, () -> acc.withdraw(-1)); // normal negative withdraw
        assertThrows(IllegalArgumentException.class, () -> acc.withdraw(-Double.MAX_VALUE)); // big negative withdraw
        
        // more than 2 decimal places
        assertThrows(IllegalArgumentException.class, () -> acc.withdraw(0.001)); // too many decimal places
        assertThrows(IllegalArgumentException.class, () -> acc.withdraw(100.999)); // too many decimal places

        // empty account
        acc.withdraw(0.01); // empty account
        assertEquals(0, acc.getBalance(), 0.001); // check balance
        assertThrows(InsufficientFundsException.class, () -> acc.withdraw(0.01)); // withdraw from empty account
    }

    // note: this doesn't cover everything in the real standard, but it's close enough
    @Test
    void isEmailValidTest(){
        // empty string
        assertFalse( BankAccount.isEmailValid(""));

        // total length
        assertFalse(BankAccount.isEmailValid("a@b.c")); // too short to be valid
        assertTrue(BankAccount.isEmailValid("a@b.co")); // shortest possible valid email

        // missing sections
        assertFalse(BankAccount.isEmailValid("@bb.com")); // missing local part
        assertFalse(BankAccount.isEmailValid("aa@.com")); // missing domain
        assertFalse(BankAccount.isEmailValid("aa@bb")); // missing tld
        
        // length of local part
        assertTrue(BankAccount.isEmailValid("a@b.com")); // 1 character
        assertFalse(BankAccount.isEmailValid("a".repeat(65) + "@b.com")); // 65 characters
        assertTrue(BankAccount.isEmailValid("a".repeat(64) + "@b.com")); // 64 characters

        // length of domain
        assertTrue(BankAccount.isEmailValid("aa@bb.com")); // 2 characters
        assertFalse(BankAccount.isEmailValid("aa@" + "a".repeat(252) + ".com")); // 256 characters
        assertTrue(BankAccount.isEmailValid("aa@" + "a".repeat(251) + ".com")); // 255 characters

        // length of tld
        assertFalse(BankAccount.isEmailValid("aa@b.c")); // 1 character
        assertTrue(BankAccount.isEmailValid("aa@bb.international")); // longest tld (13 characters)
        assertFalse(BankAccount.isEmailValid("aa@bb.internationala")); // 14 characters
        
        // domain/tld segments
        assertTrue(BankAccount.isEmailValid("aa@bb.co.uk")); // 2 part tld
        assertTrue(BankAccount.isEmailValid("aa@bb.bb.bb.bb.bb.com")); // subdomains

        // special characters local part
        assertFalse(BankAccount.isEmailValid("a\"()a@b.com")); // quotes and parentheses
        assertFalse(BankAccount.isEmailValid("a a@b.com")); // spaces
        assertTrue(BankAccount.isEmailValid("#+!%_&*@b.com")); // valid characters

        // special characters domain part
        assertFalse(BankAccount.isEmailValid("aa@b#b.com")); // invalid character in domain
        assertFalse(BankAccount.isEmailValid("aa@bb.#4")); // invalid character in tld
        assertTrue(BankAccount.isEmailValid("aa@a-b.com")); // valid hyphen in domain

        // numbers local and domain parts
        assertTrue(BankAccount.isEmailValid("4@bb.com")); // numbers in local part
        assertTrue(BankAccount.isEmailValid("aa@4.com")); // numbers in domain
        assertFalse(BankAccount.isEmailValid("aa@bb.44")); // numbers in tld

        // hyphen position local part
        assertFalse(BankAccount.isEmailValid("-aa@bb.com")); // starts with hyphen
        assertTrue(BankAccount.isEmailValid("a-a@bb.com")); // hyphen in middle
        assertFalse(BankAccount.isEmailValid("aa-@bb.com")); // ends with hyphen

        // hyphen position domain part
        assertFalse(BankAccount.isEmailValid("aa@-bb.com")); // starts with hyphen
        assertTrue(BankAccount.isEmailValid("aa@b-b.com")); // hyphen in middle
        assertFalse(BankAccount.isEmailValid("aa@bb-.com")); // ends with hyphen

        // dot position local and domain parts
        assertFalse(BankAccount.isEmailValid(".aa@bb.com")); // local starts with dot
        assertFalse(BankAccount.isEmailValid("aa@.bb.com")); // domain starts with dot
        assertFalse(BankAccount.isEmailValid("aa.@bb.com")); // local ends with dot
        assertFalse(BankAccount.isEmailValid("aa@bb.com.")); // domain ends with dot
        assertTrue(BankAccount.isEmailValid("a.a@bb.com")); // dot in middle of local part

        // double dot local and domain parts
        assertFalse(BankAccount.isEmailValid("a..a@bb.com")); // double dot in local part
        assertFalse(BankAccount.isEmailValid("aa@bb..com")); // double dot in domain

        // number of '@' symbols
        assertFalse(BankAccount.isEmailValid("a@a@b.com")); // extra @ symbol
        assertFalse(BankAccount.isEmailValid("aa.bb.com")); // missing @ symbol

        // capitalization (i think caps should be allowed, although the domain is case insensitive)
        assertTrue(BankAccount.isEmailValid("Aa@bb.com")); // capital letter in local part
        assertTrue(BankAccount.isEmailValid("aa@Bb.com")); // capital letter in domain part
        assertTrue(BankAccount.isEmailValid("AA@BB.COM")); // all caps
    }

    @Test
    void constructorTest() {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);

        assertEquals("a@b.com", bankAccount.getEmail());
        assertEquals(200, bankAccount.getBalance(), 0.001);
        //check for exception thrown correctly
        assertThrows(IllegalArgumentException.class, ()-> new BankAccount("", 100));
        assertThrows(IllegalArgumentException.class, ()-> new BankAccount("a@b.com", -50));
    }

    @Test
    void isAmountValidTest(){
        // valid amounts
        assertTrue(BankAccount.isAmountValid(0)); // zero
        assertTrue(BankAccount.isAmountValid(0.01)); // smallest valid amount
        assertTrue(BankAccount.isAmountValid(100)); // normal amount
        assertTrue(BankAccount.isAmountValid(99999999.99)); // big amount

        // invalid amounts
        assertFalse(BankAccount.isAmountValid(-Double.MIN_VALUE)); // small negative amount
        assertFalse(BankAccount.isAmountValid(-0.01)); // small negative amount
        assertFalse(BankAccount.isAmountValid(-100)); // normal negative amount
        assertFalse(BankAccount.isAmountValid(-Double.MAX_VALUE)); // big negative amount
        assertFalse(BankAccount.isAmountValid(0.001)); // too many decimal places
        assertFalse(BankAccount.isAmountValid(100.999)); // too many decimal places
    }

}