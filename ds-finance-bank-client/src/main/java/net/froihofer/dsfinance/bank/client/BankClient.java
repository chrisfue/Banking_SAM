package net.froihofer.dsfinance.bank.client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import net.froihofer.bank.common.*;

import net.froihofer.util.AuthCallbackHandler;
import net.froihofer.util.WildflyJndiLookupHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class for starting the bank client.
 *
 */
public class BankClient {
  private static Logger log = LoggerFactory.getLogger(BankClient.class);

  private Client client;
  private WebTarget baseTarget;

  public static void main(String[] args) {
    BankClient client = new BankClient();
    client.run();
  }

  public void setupRest() {
    client = ClientBuilder.newClient()
            .register(new JaxRsAuthenticator("user1","1234"))
            .register(JacksonJsonProvider.class);
    baseTarget = client.target("http://localhost:8080/ds-finance-bank-web/rs/bank");
  }

  /**
   * Skeleton method for performing an RMI lookup
   */
  private BankInterface getRmiProxy() {
    AuthCallbackHandler.setUsername("user1");
    AuthCallbackHandler.setPassword("1234");
    Properties props = new Properties();
    props.put(Context.SECURITY_PRINCIPAL,AuthCallbackHandler.getUsername());
    props.put(Context.SECURITY_CREDENTIALS,AuthCallbackHandler.getPassword());
    try {
      WildflyJndiLookupHelper jndiHelper = new WildflyJndiLookupHelper(new InitialContext(props), "ds-finance-bank-ear", "ds-finance-bank-ejb", "");
      //TODO: Lookup the proxy and assign it to some variable or return it by changing the
      //      return type of this method
      var bank =  jndiHelper.lookup("BankService", BankInterface.class);
      return bank;
    }
    catch (NamingException e) {
      log.error("Failed to initialize InitialContext.",e);
    }
    return null;
  }

  private void run() {
    BankInterface bank = getRmiProxy();
    if(bank==null) System.out.println("fuck");
    setupRest();
    System.out.println("Client Test");
    Scanner scanner = new Scanner(System.in);

    while(true) {
      System.out.println("Please enter:\n" +
              "\"Search\" to search stock\n"+
              "\"buy\" to buy stock\n"+
                      "\"sell\" to sell stock\n"+
              "\"history\" to check a stock's history\n"+
              "\"customer\" to create a new customer\n"+
              "\"csearch\" to look for existing customers\n");
      var option = scanner.nextLine().toLowerCase();

      switch (option) {
        case "search": {
          System.out.println("Enter name to search: ");
          var name = scanner.nextLine();
          var test = bank.findStockByName(name);
          System.out.println("Search result");
          for(int i = 0; i < test.size(); i++) {
            System.out.printf("%s: Symbol = %s Available shares: %d\n", test.get(i).getCompanyName(), test.get(i).getSymbol(), test.get(i).getFloatShares());
          }
          break;
        }

        case "buy": {
          System.out.println("Enter symbol: ");
          var symbol = scanner.nextLine();
          System.out.println("Enter amount: ");
          var amount = scanner.nextLine();
          try {
            int shares = Integer.parseInt(amount);
            var result = bank.buyStock(symbol, shares);
            System.out.println("Cost per share: " + result);
            System.out.println("Overall cost: " + result.multiply(BigDecimal.valueOf(shares)));

          } catch (BankException e) {
            log.error("Bank threw Exception: "+ e.getMessage());
          }  catch (Exception e) {
            log.error("Something did not work, see stack trace.", e);
            e.printStackTrace();
          }
          break;
        }

        case "sell": {
          System.out.println("Enter symbol: ");
          var symbol = scanner.nextLine();
          System.out.println("Enter amount: ");
          var amount = scanner.nextLine();
          try {

            int shares = Integer.parseInt(amount);

            WebTarget getTarget = baseTarget.path("sell")
                    .queryParam("symbol", symbol)
                    .queryParam("amount", shares);

            var response = getTarget.request(MediaType.APPLICATION_JSON_TYPE)
                    .get();

            if(response.getStatus() != Response.Status.OK.getStatusCode()) {
              throw new WebApplicationException(response.getStatusInfo().getReasonPhrase());
            }

            var res = response.readEntity(BigDecimal.class);

            System.out.println("Cost per share: " + res);
            System.out.println("Overall cost: " + res.multiply(BigDecimal.valueOf(shares)));
          }  catch (Exception e) {
            log.error("Something did not work, see stack trace.", e);
            e.printStackTrace();
          }
          break;
        }

        case "customer": {
try {
  System.out.println("Please enter First name of customer to generate;");
  String customerFirstname = scanner.nextLine();
  System.out.println("Please enter Last name of customer to generate;");
  String customerLAstName = scanner.nextLine();
  System.out.printf("Please enter Address");
  String customerAddress = scanner.nextLine();
  CustomerDTO entered = new CustomerDTO(customerFirstname, customerLAstName, customerAddress);

long newId = bank.storeCustomer(entered);
  System.out.println("Customer created, ID: " + newId );
}catch (CustomerException ce){
  log.error(("Exception thrown: " + ce.getMessage()));
}break;
        }

        case "csearch": {
          System.out.println("Enter \"Id\" to search for Id");
          String searchInput = scanner.nextLine();


          if(searchInput.equals("Id")){
            System.out.println("Enter customer ID");
            long searchId = scanner.nextLong();
           try {
             System.out.println(bank.getCustomer(searchId));

           }catch (CustomerException ce){
             log.error(("Exception thrown: " + ce.getMessage()));
           }
          }


          else{System.out.println("Enter name to search for Id");
            String searchCustomerName = scanner.nextLine();
try {
  var foundCustomers = bank.matchingCustomers(searchCustomerName);
  for(int i=0;i<foundCustomers.size();i++){
    System.out.println(foundCustomers.get(i).getFirstName() +", "+foundCustomers.get(i).getLastName()+", "
            +foundCustomers.get(i).getAddress() );
  }
}catch (CustomerException ce){
  log.error(("Exception thrown: " + ce. getMessage()));
}



          }
          break;
        }

        case "history": {
          System.out.println("Enter symbol: ");
          var symbol = scanner.nextLine();
          try {


            WebTarget getTarget = baseTarget.path("history")
                    .queryParam("symbol", symbol);

            var response = getTarget.request(MediaType.APPLICATION_JSON_TYPE)
                    .get();

            if(response.getStatus() != Response.Status.OK.getStatusCode()) {
              throw new WebApplicationException(response.getStatusInfo().getReasonPhrase());
            }
            var res = response.readEntity(new GenericType<ArrayList<PublicStockQuoteDTO>>(){});

            System.out.println("History result");
            for(int i = 0; i < res.size(); i++) {
              System.out.printf("companyName: %s: Symbol: %s Available shares: %d lastTradePrice: %s lastTradeTime: %s\n",
                      res.get(i).getCompanyName(), res.get(i).getSymbol(), res.get(i).getFloatShares(),
                      res.get(i).getLastTradePrice().toString(), res.get(i).getLastTradeTimeAsDate());
            }
          }  catch (Exception e) {
            log.error("Something did not work, see stack trace.", e);
            e.printStackTrace();
          }
          break;
        }
      }
    }
  }
}