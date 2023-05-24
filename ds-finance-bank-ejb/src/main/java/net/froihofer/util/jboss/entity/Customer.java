package net.froihofer.util.jboss.entity;


import javax.persistence.*;
import javax.ws.rs.Path;
import java.io.Serializable;

@Entity
@Table(name="Customers")
public class Customer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long CustomerId;

  //  @Column(name="Customer_Id")
    //private int customerId;

    @Column(name="first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name= "address")
    private String address;

    public Customer() {
    }

    public Customer(/*long customerId,*/ String firstName, String lastName, String address) {
     //   CustomerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public long getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(long customerId) {
        CustomerId = customerId;
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
}
