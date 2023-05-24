package net.froihofer.bank.common;

public class CustomerException  extends java.lang.Exception {

    /**
     * Creates a new instance of <code>CustomerException</code> without detail message.
     */
    public CustomerException() {
    }


    /**
     * Constructs an instance of <code>CustomerException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public CustomerException(String msg) {
        super(msg);
    }

    /**
     * Creates a new instance of <code>CustomerException</code>
     * with the specified detail message and cause.
     * @param msg the detail message (which is saved for later retrieval by the
     *            <code>getMessage()</code> method).
     * @param cause the cause (which is saved for later retrieval by the
     *              <code>getCause()</code> method).
     *              (A <code>null</code> value is permitted, and indicates that
     *              the cause is nonexistent or unknown.)
     */
    public CustomerException(String msg, Throwable cause) {
        super(msg,cause);
    }

    /**
     * Creates a new instance of <code>CustomerException</code>
     * with the specified cause.
     * @param cause the cause (which is saved for later retrieval by the
     *              <code>getCause()</code> method).
     *              (A <code>null</code> value is permitted, and indicates that
     *              the cause is nonexistent or unknown.)
     */
    public CustomerException(Throwable cause) {
        super(cause);
    }
}
