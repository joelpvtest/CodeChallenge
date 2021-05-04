package resources.CommonUtilities.UIUtilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.openqa.selenium.By;

/*
Creating an object map for Selenium tests
1) We will create a properties file named "objectmap.properties".
Format :  [logical_name]=[locator_type]:[locator_value]
                 
        Username_field = id:login_login_username
        Password_field = id:login_login_password
        Login_button = id:login_submit
        online_user=cssSelector:#sb-onlineusers > h3       
*/

public class ObjectMapManager {
  
  //property file and provide the locator information to the test.
  
  Properties properties;
  
  public ObjectMapManager(String mapFile)
  {
      properties = new Properties();
      try {
      FileInputStream Master = new FileInputStream(mapFile);
      properties.load(Master);
      Master.close();
          }catch (IOException e) {
            System.out.println(e.getMessage());
         }
      }


   public By getLocator(String ElementName) throws Exception {
         //Read value using the logical name as Key.
         String locator = properties.getProperty(ElementName);
         //Split the value which contains locator type and locator value.
         String locatorType = locator.split(":")[0];
         String locatorValue = locator.split(":")[1];
         //Return a instance of By class based on locator strategy.
           if(locatorType.toLowerCase().equals("id"))
                 return By.id(locatorValue);
           else if(locatorType.toLowerCase().equals("name"))
                 return By.name(locatorValue);
           else if((locatorType.toLowerCase().equals("classname")) || (locatorType.toLowerCase().equals("class")))
                 return By.className(locatorValue);
           else if((locatorType.toLowerCase().equals("tagname")) || (locatorType.toLowerCase().equals("tag")))
                 return By.className(locatorValue);
           else if((locatorType.toLowerCase().equals("linktext")) || (locatorType.toLowerCase().equals("link")))
                 return By.linkText(locatorValue);
           else if(locatorType.toLowerCase().equals("partiallinktext"))
                 return By.partialLinkText(locatorValue);
           else if((locatorType.toLowerCase().equals("cssselector")) || (locatorType.toLowerCase().equals("css")))
                 return By.cssSelector(locatorValue);
           else if(locatorType.toLowerCase().equals("xpath"))
                 return By.xpath(locatorValue);
           else
                   throw new Exception("Locator type '" + locatorType + "' not defined!!");
         }
}