package Services;

import net.froihofer.bank.common.*;
import net.froihofer.dsfinance.ws.trading.TradingWSException_Exception;
import net.froihofer.dsfinance.ws.trading.TradingWebService;
import net.froihofer.dsfinance.ws.trading.TradingWebServiceService;


import net.froihofer.util.jboss.entity.Customer;
import net.froihofer.util.jboss.entity.CustomerDAO;
import net.froihofer.util.jboss.entity.CustomertoDTO;
import net.froihofer.util.jboss.entity.StockToDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebService;
import javax.xml.ws.BindingProvider;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Stateless(name="BankService")
@PermitAll
/*@WebService(endpointInterface = "net.froihofer.bank.common.BankInterface", serviceName = "BankService",
        portName = "BankServicePort")*/
public class MyTradingService implements BankInterface {

    private static final Logger log = LoggerFactory.getLogger(MyTradingService.class);

    @Inject
    StockToDTO publicStockQuoteTranslator;
    private TradingWebService tradingWebService;
    public void setup() {
        log.info("Setup init!!!");
        TradingWebServiceService tradingWebServiceService = new TradingWebServiceService();
        tradingWebService = tradingWebServiceService.getTradingWebServicePort();
        BindingProvider bindingProvider = (BindingProvider) tradingWebService;
        bindingProvider.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, "git_user");
        bindingProvider.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, "git_pw");
    }

@Inject
CustomerDAO customerDAO;
    @Inject
    CustomertoDTO customertoDTO;

@Override
    public long storeCustomer(CustomerDTO customerDTO) throws CustomerException {
        if (customerDTO == null ) {
            throw new IllegalArgumentException("Customer or name cannot be empty");
        }
        try {
            Customer v = customerDAO.findById(customerDTO.getId());
            if (v == null) {
                Customer toCreate = customertoDTO.toEntity(customerDTO);

                customerDAO.persist(toCreate);

                return toCreate.getCustomerId();
            }
            else {
                customerDTO.setFirstName(customerDTO.getFirstName());
                customerDTO.setLastName(customerDTO.getLastName());
                customerDTO.setAddress(customerDTO.getAddress());
                return customerDTO.getId();
            }
        }
        catch (Exception e) {
            log.error("Problem while storing variable: "+e.getMessage(), e);
            //Do not include the root cause as classes in the stack trace might not be available on the client
            //and lead to ClassNotFoundExceptions when unmarshalling the server response.
            throw new CustomerException(e.getMessage());
        }
    }

@Override
    public String getCustomer(long id) throws CustomerException {
        try {
            Customer result = customerDAO.findById(id);
            if (result == null){ throw new CustomerException("Customer with ID:  \""+id+"\" not found.");}
            return result.getFirstName() +"\n"+ result.getLastName()+"\n"+result.getAddress();
        }
        catch (Exception e) {
            log.error("Problem while getting variable: "+e.getMessage(), e);
            //Do not include the root cause as classes in the stack trace might not be available on the client
            //and lead to ClassNotFoundExceptions when unmarshalling the server response.
            throw new CustomerException(e.getMessage());
        }
    }

    @Override
    public List<CustomerDTO> matchingCustomers(String searchTerm) throws CustomerException{
//make customerDTOs out of the cuzzies
   try{ List<Customer> found = customerDAO.getEntityManager().createQuery(
        "SELECT p FROM Customer p "+
                "WHERE p.firstName LIKE :partOfName "+
                "OR p.lastName LIKE :partOfName "+
                "ORDER BY p.lastName, p.firstName", Customer.class)
.setParameter("partOfName", "%"+searchTerm+"%")
                .getResultList();
       List<CustomerDTO> foundCustomers = new ArrayList<>();
       for(int i=0;i<found.size();i++){
           foundCustomers.add(customertoDTO.toDTO(found.get(i)));
       }
       return foundCustomers;

   }

   catch (Exception ce){
       log.error("Problem finding customer: " + ce.getMessage(), ce);
       throw new CustomerException(ce.getMessage());
   }




    }


    @Override
    public List<PublicStockQuoteDTO> findStockByName(String name) {
        setup();
        log.info("try find Stock by name: " + name);
        try {
            var quotes = tradingWebService.findStockQuotesByCompanyName(name);
            log.info("Stock size: " + quotes.size());
            List<PublicStockQuoteDTO> quoteDTOs = new ArrayList<>();
            for(int i = 0; i < quotes.size(); i++) {
                quoteDTOs.add(publicStockQuoteTranslator.toPublicStockQuoteDTO(quotes.get(i)));
            }
            return quoteDTOs;
        } catch (Exception e) {
            log.error(e.toString(), e);
        }
        return null;
    }



    @Override
    public BigDecimal buyStock(String symbol, int shares) throws BankException {
        setup();
        log.info("try buy Stock by symbol: " + symbol + " Amount: " + shares);

        try {

            return tradingWebService.buy(symbol, shares);

        }
        catch (TradingWSException_Exception e) {
            log.error("Bank threw Exception: " + e.getMessage());
            throw new BankException(e.getMessage());
        }
    }
}