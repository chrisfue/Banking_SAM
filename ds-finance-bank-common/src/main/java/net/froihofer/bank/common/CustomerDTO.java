package net.froihofer.bank.common;

import java.io.Serializable;

public class CustomerDTO implements Serializable {

    private long id;

    private String firstName;

    private String lastName;

    private String Address;


    public CustomerDTO() {
    }

    public CustomerDTO(/*long id,*/ String firstName, String lastName, String address) {
       // this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        Address = address;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
