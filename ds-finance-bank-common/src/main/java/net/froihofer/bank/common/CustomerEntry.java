package net.froihofer.bank.common;

public class CustomerEntry {
    private String firstName;
    private String lastName;
    private String address;
    private long customerNo;

    public CustomerEntry(String firstName, String lastName, String address, long customerNo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.customerNo = customerNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(long customerNo) {
        this.customerNo = customerNo;
    }
}
