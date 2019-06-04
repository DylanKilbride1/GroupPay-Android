package grouppay.dylankilbride.com.grouppay;

import org.junit.Test;

import grouppay.dylankilbride.com.activities.EnterPaymentMethodDetails;

import static org.junit.Assert.*;

public class CardOCRParser {

  @Test
  public void parseOCRExpiryDateTest() {
    EnterPaymentMethodDetails epd = new EnterPaymentMethodDetails();
    assertEquals(epd.parseOCRCardExpiryDate(5, 2020), "05/20");
    assertEquals(epd.parseOCRCardExpiryDate(5, 20), "05/20");
    assertEquals(epd.parseOCRCardExpiryDate(10, 20), "10/20");
  }


}