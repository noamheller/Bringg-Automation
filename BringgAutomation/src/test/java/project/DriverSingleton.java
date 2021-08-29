package project;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.sql.Connection;

/**
 * a Singelton class for creating one instance of driver.
 * driver initiation depends on config table in DB, or XML file (when there is no connection to DB)
 */
public class DriverSingleton {
    private static WebDriver driver;
    private static Connection con;
    private static ActionsDB actionsDB = new ActionsDB();

    public static WebDriver getDriverInstance() throws Exception {
        if (driver == null) {
            String browser = null;
            try {
                con = ConnectionSingleton.getConnectionInstance();
                browser = actionsDB.getConfigDataById(con, 2);
            } catch (Exception e) {
                browser = getData("browserType");
                e.printStackTrace();
            }finally {
                if (browser.equals("Chrome")) {
                    System.setProperty("webdriver.chrome.driver", "/Users/noamheller/" +
                            "Documents/NOAM/WORK/Automation Course/chromedriver");
                    driver = new ChromeDriver();
                } else if (browser.equals("FireFox")) {
                    System.setProperty("webdriver.firefox.driver", "C:\\.exe");
                    driver = new FirefoxDriver();
                }
            }
        }return driver;
    }

    /**
     * get data from xml file
     * @param keyName tag name in xml file of browser type
     * @return driver kind
     * @throws Exception
     */
    public static String getData(String keyName) throws Exception {
        File fXmlFile = new File("/Users/noamheller/Documents/NOAM/WORK/Automation" +
                " Course/Project2/src/main/resources/data.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        assert doc != null;
        return doc.getElementsByTagName(keyName).item(0).getTextContent();
    }
}
