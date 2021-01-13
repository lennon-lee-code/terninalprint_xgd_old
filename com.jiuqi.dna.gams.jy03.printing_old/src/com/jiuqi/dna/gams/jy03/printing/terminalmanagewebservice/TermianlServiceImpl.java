package com.jiuqi.dna.gams.jy03.printing.terminalmanagewebservice;

import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jws.WebService;
import javax.print.PrintService;

import org.springframework.stereotype.Component;

import com.jiuqi.dna.gams.jy03.printing.util.ConstsUtil;
import com.jiuqi.dna.gams.jy03.printing.util.Response;

@WebService(
serviceName="TerminalService", 
targetNamespace="Terminal",
endpointInterface="com.jiuqi.dna.gams.jy03.printing.terminalmanagewebservice.TerminalService")
@Component("terminalService")
public class TermianlServiceImpl implements TerminalService{

	@Override
	public Response getPrinterStatus() {
		String billPrinter = ConstsUtil.billPrinterName;
		String barPrinter = ConstsUtil.barPrinterName;
		PrintService[] printServices = PrinterJob.lookupPrintServices();
		Map<String, String> statusMap = new HashMap<String, String>();
		statusMap.put("billPrinterStatus", "正常");
		statusMap.put("barPrinterStatus", "正常");
		if(printServices == null || printServices.length == 0) {
			statusMap.clear();
			statusMap.put("billPrinterStatus", "未安装");
			statusMap.put("barPrinterStatus", "未安装");
			return Response.success(statusMap);
		}else {
			List<String> names = new ArrayList<String>();
			for(int i = 0; i < printServices.length; i++) {
				names.add(printServices[i].getName());
			}
			if(!names.contains(billPrinter)) {
				statusMap.remove("billPrinterStatus");
				statusMap.put("billPrinterStatus", "异常");
			};
            if(!names.contains(barPrinter)) {
            	statusMap.remove("barPrinterStatus");
				statusMap.put("barPrinterStatus", "异常");
			};
		}
		return Response.success(statusMap);
	}
}
