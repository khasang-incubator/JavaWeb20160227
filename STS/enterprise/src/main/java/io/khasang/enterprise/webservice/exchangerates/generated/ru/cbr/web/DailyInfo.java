package io.khasang.enterprise.webservice.exchangerates.generated.ru.cbr.web;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 *
 *
 *
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 *
 */
@WebServiceClient(name = "DailyInfo", targetNamespace = "http://web.cbr.ru/", wsdlLocation = "DailyInfo.asmx.xml")
public class DailyInfo
        extends Service
{

    private final static URL DAILYINFO_WSDL_LOCATION;
    private final static WebServiceException DAILYINFO_EXCEPTION;
    private final static QName DAILYINFO_QNAME = new QName("http://web.cbr.ru/", "DailyInfo");

    static {
        DAILYINFO_WSDL_LOCATION = io.khasang.enterprise.webservice.exchangerates.generated.ru.cbr.web.DailyInfo.class.getClassLoader().getResource("DailyInfo.asmx.xml");
        WebServiceException e = null;
        if (DAILYINFO_WSDL_LOCATION == null) {
            e = new WebServiceException("Cannot find 'META-INF/DailyInfo.asmx.xml' wsdl. Place the resource correctly in the classpath.");
        }
        DAILYINFO_EXCEPTION = e;
    }

    public DailyInfo() {
        super(__getWsdlLocation(), DAILYINFO_QNAME);
    }

    public DailyInfo(WebServiceFeature... features) {
        super(__getWsdlLocation(), DAILYINFO_QNAME, features);
    }

    public DailyInfo(URL wsdlLocation) {
        super(wsdlLocation, DAILYINFO_QNAME);
    }

    public DailyInfo(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, DAILYINFO_QNAME, features);
    }

    public DailyInfo(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public DailyInfo(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns DailyInfoSoap
     */
    @WebEndpoint(name = "DailyInfoSoap")
    public DailyInfoSoap getDailyInfoSoap() {
        return super.getPort(new QName("http://web.cbr.ru/", "DailyInfoSoap"), DailyInfoSoap.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns DailyInfoSoap
     */
    @WebEndpoint(name = "DailyInfoSoap")
    public DailyInfoSoap getDailyInfoSoap(WebServiceFeature... features) {
        return super.getPort(new QName("http://web.cbr.ru/", "DailyInfoSoap"), DailyInfoSoap.class, features);
    }

    private static URL __getWsdlLocation() {
        if (DAILYINFO_EXCEPTION!= null) {
            throw DAILYINFO_EXCEPTION;
        }
        return DAILYINFO_WSDL_LOCATION;
    }

}