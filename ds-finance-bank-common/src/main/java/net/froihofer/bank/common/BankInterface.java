package net.froihofer.bank.common;


import javax.ejb.Remote;
import java.math.BigDecimal;
import java.util.List;


@Remote
public interface BankInterface {
public List<PublicStockQuoteDTO> findStockByName(String name);
public BigDecimal buyStock(String Symbol, int shares)throws BankException;

    public long storeCustomer(CustomerDTO customerDTO) throws CustomerException;

    public  String getCustomer(long id) throws CustomerException;

    public List<CustomerDTO> matchingCustomers(String searchTerm) throws CustomerException;

}
