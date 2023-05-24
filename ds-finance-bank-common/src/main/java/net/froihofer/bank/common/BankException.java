package net.froihofer.bank.common;



public class BankException extends Exception {
    public BankException() {

    }

    public BankException(String msg) {
        super(msg);
    }

    public BankException(String msg, Throwable cause) {
        super(msg, cause);
    }
    public BankException(Throwable cause) {
        super(cause);
    }
}