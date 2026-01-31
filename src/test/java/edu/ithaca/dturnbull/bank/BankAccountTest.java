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
    }

}