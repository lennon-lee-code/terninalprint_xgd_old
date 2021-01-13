package com.jiuqi.dna.gams.jy03.printing.terminalmanagewebservice;


import javax.jws.WebMethod;
import javax.jws.WebService;
import com.jiuqi.dna.gams.jy03.printing.util.Response;

@WebService
public interface TerminalService {
	   @WebMethod
       public Response getPrinterStatus();
}
