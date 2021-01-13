package com.jiuqi.dna.gams.jy03.printing.terminalmanagewebservice;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;

@WebServiceClient(name = "TerminalManageWebservice", 
                  targetNamespace = "TerminalManageWebservice") 
public class TerminalManageWebservice extends Service{
    public final static QName SERVICE = new QName("TerminalManageWebservice", "TerminalManageWebservice");
    public final static QName TerminalManageWebservicePort = new QName("TerminalManageWebservice", "TerminalManageWebservicePort");
	public TerminalManageWebservice(URL wsdlDocumentLocation) {
		super(wsdlDocumentLocation, SERVICE);
	}
    
	@WebEndpoint(name = "TerminalManageWebservicePort")
    public TerminalManage getTerminalManagePort() {
        return super.getPort(TerminalManageWebservicePort, TerminalManage.class);
    }
}
