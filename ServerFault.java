package lpi.server.soap;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "ServerFault", targetNamespace = "http://soap.server.lpi/")
public class ServerFault
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private ServerException faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public ServerFault(String message, ServerException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public ServerFault(String message, ServerException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: lpi.server.soap.ServerException
     */
    public ServerException getFaultInfo() {
        return faultInfo;
    }

}