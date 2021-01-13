package com.jiuqi.dna.gams.jy03.printing.terminalmanagewebservice;

import java.util.UUID;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.Response;
import javax.xml.ws.ResponseWrapper;

@WebService(targetNamespace = "http://service.webservice.terminalprint.gams2.np.jiuqi.com/", name = "TerminalManageWebservice")
public interface TerminalManage {
	    @SuppressWarnings("rawtypes")
	    @WebMethod
	    @RequestWrapper(localName = "addPrintInfo", targetNamespace = "http://service.webservice.terminalprint.gams2.np.jiuqi.com/", className = "com.jiuqi.np.gams2.terminalprint.webservice.service.TerminalManageWebservice.addPrintInfo")
	    @ResponseWrapper(localName = "addPrintInfoResponse", targetNamespace = "http://service.webservice.terminalprint.gams2.np.jiuqi.com/", className = "com.jiuqi.np.gams2.terminalprint.webservice.service.TerminalManageWebservice.addPrintInfoResponse")
	    @WebResult(name = "return", targetNamespace = "")
	    public String addPrintInfo(
	        @WebParam(name="macid") String macid, 
			@WebParam(name="dayrid") UUID dayrid,
			@WebParam(name="dayyw") String dayyw,
			@WebParam(name="daylx") String daylx,
			@WebParam(name="daygs") int daygs,
			@WebParam(name="daycs") int daycs
	    );
}
