
/*
 * 
 */

package com.jiuqi.dna.gams.jy03.printing.loginwebservice;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import com.jiuqi.dna.gams.jy03.printing.loginwebservice.PrintingTerminalLogin;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.3.3
 * 2018-05-10T15:49:49.022+08:00
 * Generated source version: 2.3.3
 * 
 */


@WebServiceClient(name = "PrintTerminalLoginService", 
                  targetNamespace = "PrintTerminal") 
public class PrintingTerminalLoginWebService extends Service {

    public final static QName SERVICE = new QName("PrintTerminal", "PrintTerminalLoginService");
    public final static QName PrintingTerminalLoginPort = new QName("PrintTerminal", "PrintTerminalLoginPort");
    public PrintingTerminalLoginWebService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }
    
    /**
     * 
     * @return
     *     returns PrintingTerminalLogin
     */
    @WebEndpoint(name = "PrintingTerminalLoginPort")
    public PrintingTerminalLogin getPrintingTerminalLoginPort() {
        return super.getPort(PrintingTerminalLoginPort, PrintingTerminalLogin.class);
    }

}