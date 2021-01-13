package com.jiuqi.dna.gams.jy03.printing.controller;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jiuqi.dna.gams.jy03.printing.billcommonwebservice.BillCommonFieldsEntity;
import com.jiuqi.dna.gams.jy03.printing.billcommonwebservice.PrintingTerminalBillCommon;
import com.jiuqi.dna.gams.jy03.printing.billcommonwebservice.PrintingTerminalBillCommonWebService;
import com.jiuqi.dna.gams.jy03.printing.cardwebservice.AssetCard;
import com.jiuqi.dna.gams.jy03.printing.cardwebservice.PrintingTerminalAssetCard;
import com.jiuqi.dna.gams.jy03.printing.cardwebservice.PrintingTerminalAssetCardWebService;
import com.jiuqi.dna.gams.jy03.printing.loginwebservice.PrintingTerminalLogin;
import com.jiuqi.dna.gams.jy03.printing.loginwebservice.PrintingTerminalLoginWebService;
import com.jiuqi.dna.gams.jy03.printing.setting.PrintingTerminalSetting;
import com.jiuqi.dna.gams.jy03.printing.setting.PrintingTerminalSettingWebService;
import com.jiuqi.dna.gams.jy03.printing.setting.SettingObject;
import com.jiuqi.dna.gams.jy03.printing.terminalmanagewebservice.TerminalManage;
import com.jiuqi.dna.gams.jy03.printing.terminalmanagewebservice.TerminalManageWebservice;
import com.jiuqi.dna.gams.jy03.printing.util.ConstsUtil;
import com.jiuqi.dna.gams.jy03.printing.util.PDFPrintUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@Scope(value="prototype")// 默认为单例模式
public class MainController {

	private static Map<String, String> onlineUsername = new HashMap<String, String>();
	private static Map<String, String> onlineFunctions = new HashMap<String, String>();
	private static UUID userID = null;//登录人id

	@RequestMapping("/toLogin")
	public String toLogin() {
		return "login";
	}
	
	@RequestMapping("/toNFCLogin")
	public String toNFCLogin() {
		return "NFClogin";
	}

	@RequestMapping(value = "/doLogin", produces = "text/html; charset=UTF-8")
	@ResponseBody
	public String doLogin(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("username") String username, @RequestParam("password") String password)
			throws MalformedURLException, UnsupportedEncodingException {
		PrintingTerminalLoginWebService loginWebService = new PrintingTerminalLoginWebService(new URL(ConstsUtil.loginWebServiceUrl));
		PrintingTerminalLogin loginPort = loginWebService.getPrintingTerminalLoginPort();
		String checkUser = loginPort.checkUser(username, password);
		if (checkUser != null) {
			JSONObject fromObject = JSONObject.fromObject(checkUser);
			boolean isSuccessful = fromObject.getBoolean("isSuccessful");
			if (isSuccessful) {
				//当前用户id
				userID = UUID.fromString(fromObject.getString("userID"));
				String functions = fromObject.getString("functions");
				String curSessionId = request.getSession().getId();
				if (!MainController.onlineUsername.containsKey(curSessionId)) {
					MainController.onlineUsername.put(curSessionId, username);
				} else {
					JSONObject json = new JSONObject();
					json.put("isSuccessful", false);
					json.put("message", "当前用户已在线，不允许重复登录！");
					return json.toString();
				}
				//MainController.onlineUsername.put(curSessionId, username);
					
				if (!MainController.onlineFunctions.containsKey(curSessionId)) {
					MainController.onlineFunctions.put(curSessionId, functions);
				}
			}
		}

		return checkUser;
	}
	@RequestMapping(value = "/doNFCLogin", produces = "text/html; charset=UTF-8")
	@ResponseBody
	public String doNFCLogin(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("wlkh") String wlkh)
			throws MalformedURLException, UnsupportedEncodingException {
		PrintingTerminalLoginWebService loginWebService = new PrintingTerminalLoginWebService(new URL(ConstsUtil.loginWebServiceUrl));
		PrintingTerminalLogin loginPort = loginWebService.getPrintingTerminalLoginPort();
		String campuscard = loginPort.checkCampusCard(wlkh);
		if (campuscard != null) {
			JSONObject fromObject = JSONObject.fromObject(campuscard);
			boolean isSuccessful = fromObject.getBoolean("isSuccessful");
			if (isSuccessful) {
				//当前用户id
				userID = UUID.fromString(fromObject.getString("userID"));
				String functions = fromObject.getString("functions");
				String username = fromObject.getString("username");
				String curSessionId = request.getSession().getId();
				if (!MainController.onlineUsername.containsKey(curSessionId)) {
					MainController.onlineUsername.put(curSessionId, username);
				} else {
					JSONObject json = new JSONObject();
					json.put("isSuccessful", false);
					json.put("message", "当前用户已在线，不允许重复登录！");
					return json.toString();
				}
				
				if (!MainController.onlineFunctions.containsKey(curSessionId)) {
					MainController.onlineFunctions.put(curSessionId, functions);
				}
			}
		}

		return campuscard;
	}

	@RequestMapping("/doLogout")
	public String doLogout(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		System.out.println(session.getMaxInactiveInterval());
		if (session != null) {
			System.out.println(session.getId() + " " + MainController.onlineUsername.get(session.getId()) + "注销成功");
			MainController.onlineUsername.remove(session.getId());
			MainController.onlineFunctions.remove(session.getId());
			session.invalidate();
		}
		return "index";
	}

	@RequestMapping("/toFunction")
	public String toFunction() {
		return "functions";
	}

	@RequestMapping(value = "/getFunctions", produces = "text/html; charset=UTF-8")
	@ResponseBody
	public String getFunctions(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		json.put("functions", MainController.onlineFunctions.get(request.getSession().getId()));
		return json.toString();
	}

	@RequestMapping("/toList")
	public ModelAndView toList(@RequestParam("functionName") String functionName) {
		ModelAndView modelAndView = new ModelAndView();
		if ("assetCard".equals(functionName)) {
			modelAndView.setViewName("cardList");
		} else if("ownAssetCard".equals(functionName)){
			modelAndView.setViewName("ownCardList");
		}else {
			modelAndView.setViewName("billList");
		}
		return modelAndView;
	}

	// =======================卡片=======================

	@RequestMapping(value = "/getAssetCard", method=RequestMethod.POST,produces = "text/html; charset=UTF-8")
	@ResponseBody
	public String getAssetCard(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name = "pageIndex") String pageIndex, @RequestParam(name = "pageSize") String pageSize,
			@RequestParam(name = "value") String value) throws MalformedURLException {
		//调用webservice接口，获取卡片
		PrintingTerminalAssetCardWebService assetCardWebService = new PrintingTerminalAssetCardWebService(
				new URL(ConstsUtil.cardWebServiceUrl));
		PrintingTerminalAssetCard assetCardPort = assetCardWebService.getPrintingTerminalAssetCardPort();
		if (!StringUtils.isEmpty(value)) {
			value = value.trim();
		}

		String username = MainController.onlineUsername.get(request.getSession().getId());
		List<AssetCard> cardList = assetCardPort.getCardList(username, Integer.valueOf(pageIndex),
				Integer.valueOf(pageSize), value);
		JSONArray result = JSONArray.fromObject(cardList);
		return result.toString();
	}

	@RequestMapping(value = "/printAssetCard", produces = "text/html; charset=UTF-8")
	@ResponseBody
	public String printAssetCard(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String copyCount = request.getParameter("copyCount");// 打印份数
		String printType = request.getParameter("printType");// printType决定打印条码还是单据，label：条码；bill：单据
		String printerName = printType.equals("bill") ? ConstsUtil.billPrinterName : ConstsUtil.barPrinterName;
		// 打印前先检查剩余纸张是否够用
	/*	int paperPieces = Integer.valueOf(request.getParameter("paperPieces"));// 本次打印耗费纸张数量
		SettingObject settingObj = new SettingObject();
		settingObj.setPrinterName(printerName);
		settingObj.setPrintingNum(paperPieces);
		settingObj.setUsername(onlineUsername.get(request.getSession().getId()));
		
		PrintingTerminalSettingWebService settingWebService = new PrintingTerminalSettingWebService(new URL(ConstsUtil.settingWebServiceUrl));
		PrintingTerminalSetting settingPort = settingWebService.getPrintingTerminalSettingPort();
		SettingObject result = settingPort.queryMinRemainingNumSetting(settingObj);
		if(result.getSettingNum() < paperPieces) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("code", 3);
			String changeType = PDFPrintUtil.getChineseChangeType(result.getChangeType());
			jsonObject.put("message", "打印机" + settingObj.getPrinterName() + changeType + "的预计可打印张数已不足" + paperPieces + "，请及时更换。");
			return jsonObject.toString();
		}*/
		
		String data = request.getParameter("data");
		JSONArray jsonArray = JSONArray.fromObject(data);
		@SuppressWarnings({ "unchecked" })
		List<AssetCard> cardList = (List<AssetCard>) JSONArray.toCollection(jsonArray, AssetCard.class);
		PrintingTerminalAssetCardWebService assetCardWebService = new PrintingTerminalAssetCardWebService(new URL(ConstsUtil.cardWebServiceUrl));
		PrintingTerminalAssetCard assetCardPort = assetCardWebService.getPrintingTerminalAssetCardPort();
		List<byte[]> pdfDataList = null;
		
		try {
			pdfDataList = assetCardPort.getPDFData(cardList, printType);   //np平台读取打印模板
		} catch (Exception e) {
			return PDFPrintUtil.getPrintMessage(false, "打印失败，找不到该标识的打印模板，请联系管理员..").toString();
		}
		if(pdfDataList ==null || pdfDataList.size() == 0 ) {
			return PDFPrintUtil.getPrintMessage(false, "打印失败，找不到该标识的打印模板，请联系管理员..").toString();
		}
		
		for(int i=0;i<pdfDataList.size();i++) {
			// 以下用于测试环境，查看生成的pdf文档
			//PDFPrintUtil.getNewPDFPath(pdfDataList.get(i),i);
	 		//PDFPrintUtil.deleteFile(newPDFPath);
		    JSONObject jsonObject = PDFPrintUtil.doPrintByPDFBox(pdfDataList.get(i), printerName, Integer.valueOf(copyCount));
			if(jsonObject.get("code").toString().equals("0")) {
				//打印失败
				return jsonObject.toString();
			}else if(jsonObject.get("code").toString().equals("1")) {
				//settingPort.updatePrintedNum(settingObj);
			}
		}
		//推送打印记录到NP
		/*TerminalManageWebservice terminalManageWebservice = new TerminalManageWebservice(new URL(ConstsUtil.terminalManageWebserviceUrl));
		TerminalManage terminalManagePort = terminalManageWebservice.getTerminalManagePort();
		String daylx = "billPrint";
		if("bill".equals(printType)) {
			daylx = "billPrint";
		}else {
			daylx = "labelPrint";
		}
		String dayyw = "资产打印";
		int daygs = pdfDataList.size();
		int daycs = Integer.parseInt(copyCount);
		UUID dayrid = userID;
		terminalManagePort.addPrintInfo(ConstsUtil.MACADDRESS, dayrid, dayyw, daylx, daygs, daycs);*/
		return PDFPrintUtil.getPrintMessage(true, "打印成功。").toString();
	}

	// =======================单据=======================

	@RequestMapping(value = "/getDataList", method=RequestMethod.POST,produces = "text/html; charset=UTF-8")
	@ResponseBody
	public String getDataList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name = "functionName") String functionName,
			@RequestParam(name = "pageIndex") String pageIndex, @RequestParam(name = "pageSize") String pageSize,
			@RequestParam(name = "value") String value, @RequestParam(name = "workflowstate") String workflowstate)
			throws MalformedURLException {

		JSONArray result = null;

		if (!StringUtils.isEmpty(functionName)) {
			String billDefine = ConstsUtil.funtionName_tableName_mapping.get(functionName);
			System.out.println("billDefine: " + billDefine);
			if (!StringUtils.isEmpty(value)) {
				value = value.trim();
			}
			PrintingTerminalBillCommonWebService billCommonWebService = new PrintingTerminalBillCommonWebService(new URL(ConstsUtil.billWebServiceUrl));
			PrintingTerminalBillCommon billCommonPort = billCommonWebService.getPrintingTerminalBillCommonPort();

			String username = MainController.onlineUsername.get(request.getSession().getId());
			List<BillCommonFieldsEntity> dataList = billCommonPort.getDataList(username, Integer.valueOf(pageIndex),
					Integer.valueOf(pageSize), value, workflowstate, billDefine);
			result = JSONArray.fromObject(dataList);
		}

		if (result != null) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("functionTitle", ConstsUtil.functionName_functionTitle_mapping.get(functionName));
			result.add(result.size(), jsonObject);
		}
		return result == null ? null : result.toString();
	}

	@RequestMapping(value = "/printBillOrLabel", produces = "text/html; charset=UTF-8")
	@ResponseBody
	public String printBillOrLabel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String copyCount = request.getParameter("copyCount");// 打印份数
		String printType = request.getParameter("printType");// printType决定打印条码还是单据，label：条码；bill：单据
		String printName = request.getParameter("printName");//打印模板,可为空串
		String printerName = printType.equals("bill") ? ConstsUtil.billPrinterName : ConstsUtil.barPrinterName;
		System.out.println(printerName);
		
				// 打印前先检查剩余纸张是否够用
			/*	int paperPieces = Integer.valueOf(request.getParameter("paperPieces"));// 本次打印耗费纸张数量
				SettingObject settingObj = new SettingObject();
				settingObj.setPrinterName(printerName);
				settingObj.setPrintingNum(paperPieces);
				settingObj.setUsername(onlineUsername.get(request.getSession().getId()));
				
				PrintingTerminalSettingWebService settingWebService = new PrintingTerminalSettingWebService(new URL(ConstsUtil.settingWebServiceUrl));
				PrintingTerminalSetting settingPort = settingWebService.getPrintingTerminalSettingPort();
				SettingObject result = settingPort.queryMinRemainingNumSetting(settingObj);
				if(result.getSettingNum() < paperPieces) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("code", 3);
					String changeType = PDFPrintUtil.getChineseChangeType(result.getChangeType());
					jsonObject.put("message", "打印机" + settingObj.getPrinterName() + changeType + "的预计可打印张数已不足" + paperPieces + "，请及时更换。");
					return jsonObject.toString();
				}*/
		String data = request.getParameter("data");
		JSONArray jsonArray = JSONArray.fromObject(data);
		@SuppressWarnings({ "unchecked" })
		List<BillCommonFieldsEntity> dataList = (List<BillCommonFieldsEntity>) JSONArray.toCollection(jsonArray, BillCommonFieldsEntity.class);

		PrintingTerminalBillCommonWebService billCommonWebService = new PrintingTerminalBillCommonWebService(new URL(ConstsUtil.billWebServiceUrl));
		PrintingTerminalBillCommon billCommonPort = billCommonWebService.getPrintingTerminalBillCommonPort();
		
		List<byte[]> pdfDataList = null; 
		try {
			pdfDataList = billCommonPort.getPDFData(dataList, printType,printName); //NP平台打印模板读取失败
		} catch (Exception e) {
			return PDFPrintUtil.getPrintMessage(false, "打印失败，找不到该标识的打印模板，请联系管理员..").toString();
		}
		if(pdfDataList ==null || pdfDataList.size() ==0) {
			return PDFPrintUtil.getPrintMessage(false, "打印失败，找不到该标识的打印模板，请联系管理员..").toString();
		}
		for(int i=0;i<pdfDataList.size();i++) {
			// 以下用于测试环境，查看生成的pdf文档
			//@SuppressWarnings("unused")
			//String newPDFPath = PDFPrintUtil.getNewPDFPath(pdfDataList.get(i), i);
	 		//PDFPrintUtil.deleteFile(newPDFPath);
		    JSONObject jsonObject = PDFPrintUtil.doPrintByPDFBox(pdfDataList.get(i), printerName, Integer.valueOf(copyCount));
			if(jsonObject.get("code").toString().equals("0")) {
				//打印失败
				return jsonObject.toString();
			}
			else if(jsonObject.get("code").toString().equals("1")) {
				//settingPort.updatePrintedNum(settingObj);
			}
		}
		//推送打印记录到NP
		/*TerminalManageWebservice terminalManageWebservice = new TerminalManageWebservice(new URL(ConstsUtil.terminalManageWebserviceUrl));
		TerminalManage terminalManagePort = terminalManageWebservice.getTerminalManagePort();
		String daylx = "billPrint";
		String dayyw = "单据打印";
		if("bill".equals(printType)) {
			daylx = "billPrint";
		}else {
			daylx = "labelPrint";
			dayyw = "资产打印";
		}
		int daygs = pdfDataList.size();
		int daycs = Integer.parseInt(copyCount);
		UUID dayrid = userID;
		terminalManagePort.addPrintInfo(ConstsUtil.MACADDRESS, dayrid, dayyw, daylx, daygs, daycs);*/
		return PDFPrintUtil.getPrintMessage(true, "打印成功。").toString();
	}
	// =======================名下资产=======================

	@RequestMapping("/toOwnAssetCard")
	public String toOwnAssetCard() {
		return "ownCardList";
	}

	@RequestMapping(value = "/getOwnAssetCard", method=RequestMethod.POST,produces = "text/html; charset=UTF-8")
	@ResponseBody
	public String getOwnAssetCard(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name = "pageIndex") String pageIndex, @RequestParam(name = "pageSize") String pageSize,
			@RequestParam(name = "value") String value) throws MalformedURLException {
		// 调用webservice接口，获取卡片
		PrintingTerminalAssetCardWebService assetCardWebService = new PrintingTerminalAssetCardWebService(new URL(ConstsUtil.cardWebServiceUrl));
		PrintingTerminalAssetCard assetCardPort = assetCardWebService.getPrintingTerminalAssetCardPort();
		if (!StringUtils.isEmpty(value)) {
			value = value.trim();
		}

		String username = MainController.onlineUsername.get(request.getSession().getId());
		List<AssetCard> ownCardList = assetCardPort.getOwnCardList(username, Integer.valueOf(pageIndex), Integer.valueOf(pageSize), value);
		JSONArray result = JSONArray.fromObject(ownCardList);
		return result.toString();
	}
	
	
	// =======================日常运营=======================
	@RequestMapping(value = "/setSettingNum")
	public void setSettingNum(HttpServletRequest request, HttpServletResponse response) throws MalformedURLException {
		String data = request.getParameter("data");
		JSONObject jsonObject = JSONObject.fromObject(data);
		SettingObject settingObj = (SettingObject) JSONObject.toBean(jsonObject, SettingObject.class);
		settingObj.setUsername(onlineUsername.get(request.getSession().getId()));
		settingObj.setPrintingNum(0);
		
		String printType = request.getParameter("printType");
		if(printType.equals("bill")) {
			settingObj.setPrinterName(ConstsUtil.billPrinterName);
		}else if(printType.equals("label")) {
			settingObj.setPrinterName(ConstsUtil.barPrinterName);
		}
		
		PrintingTerminalSettingWebService settingWebService = new PrintingTerminalSettingWebService(new URL(ConstsUtil.settingWebServiceUrl));
		PrintingTerminalSetting settingPort = settingWebService.getPrintingTerminalSettingPort();
		settingPort.setSettingNum(settingObj);
	}
	
	@RequestMapping(value = "/querySetting", produces = "text/html; charset=UTF-8")
	@ResponseBody
	public String querySetting(HttpServletRequest request, HttpServletResponse response) throws MalformedURLException {
		String data = request.getParameter("data");
		JSONObject jsonObject = JSONObject.fromObject(data);
		SettingObject settingObj = (SettingObject) JSONObject.toBean(jsonObject, SettingObject.class);
		settingObj.setUsername(onlineUsername.get(request.getSession().getId()));
		
		String printType = request.getParameter("printType");
		if(printType.equals("bill")) {
			settingObj.setPrinterName(ConstsUtil.billPrinterName);
		}else if(printType.equals("label")) {
			settingObj.setPrinterName(ConstsUtil.barPrinterName);
		}
		
		PrintingTerminalSettingWebService settingWebService = new PrintingTerminalSettingWebService(new URL(ConstsUtil.settingWebServiceUrl));
		PrintingTerminalSetting settingPort = settingWebService.getPrintingTerminalSettingPort();
		SettingObject result = settingPort.querySetting(settingObj);
		
		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put("result", result);
		
		return jsonObject2.toString();
	}
	
	@RequestMapping(value = "/getPrintModel", method=RequestMethod.POST,produces = "text/html; charset=UTF-8")
	@ResponseBody
	public String getPrintModel(HttpServletRequest request, HttpServletResponse response) throws MalformedURLException{ 
		String functionName = request.getParameter("functionName");
		String billDefine = ConstsUtil.funtionName_tableName_mapping.get(functionName);
		String printType = request.getParameter("printType");
		List<String>  result = new ArrayList<String>();
		PrintingTerminalBillCommonWebService printing = new PrintingTerminalBillCommonWebService(new URL(ConstsUtil.billWebServiceUrl));
		PrintingTerminalBillCommon billCommonPort =  printing.getPrintingTerminalBillCommonPort();
		result = billCommonPort.getPrintModel(billDefine, printType);
		JSONObject json = new JSONObject();
		json.put("result",result);
		return json.toString();
	}
	
	@GetMapping(value = "/isPrintJdb")
	@ResponseBody
	public String isPrintJdb(HttpServletRequest request, HttpServletResponse response) throws MalformedURLException {
		String billId = request.getParameter("billId");
		String reportoid = request.getParameter("billDefine");
		String billDefine = ConstsUtil.funtionName_tableName_mapping.get(reportoid);
		PrintingTerminalBillCommonWebService billCommonWebService = new PrintingTerminalBillCommonWebService(new URL(ConstsUtil.billWebServiceUrl));
		PrintingTerminalBillCommon billCommonPort = billCommonWebService.getPrintingTerminalBillCommonPort();
		Boolean checkHasTenW = billCommonPort.checkHasTenW(billId, billDefine);
		return checkHasTenW.toString();	
	}
}
