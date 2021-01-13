package com.jiuqi.dna.gams.jy03.printing.util;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * 接口路径及主页面单据显示配置文件
 * @author Fengjs
 *
 */
public class ConstsUtil {
	
	/**
	 * 功能标识-主表标识映射       改为功能和单据define的映射--20190905  李瑞奇
	 */
	public static Map<String, String> funtionName_tableName_mapping = new HashMap<String, String>();
	static {
		
		funtionName_tableName_mapping.put("checkIn", "com.jiuqi.np.gams2.business.bill.ReimburseBillDefine"); //西工大
		//funtionName_tableName_mapping.put("checkIn", "com.jiuqi.np.gams2.business.bill.InspectionEntryBillDefine"); 标准版
		funtionName_tableName_mapping.put("exchange", "com.jiuqi.np.gams2.business.bill.AssetadjustmentBillDefine");
		funtionName_tableName_mapping.put("commonChange", "com.jiuqi.np.gams2.business.bill.NormalAssetChangeBillDefine");
		funtionName_tableName_mapping.put("importantChange", "com.jiuqi.np.gams2.business.bill.ImportantAssetChangeBillDefine");
		funtionName_tableName_mapping.put("assetChange", "com.jiuqi.np.gams2.business.bill.ClassAssetChangeBillDefine");//资产分类变动单据打印
		funtionName_tableName_mapping.put("innerTransfer", "com.jiuqi.np.gams2.business.bill.InnerTransferBillDefine");//部门内资产调拨单据打印
		funtionName_tableName_mapping.put("transfer", "com.jiuqi.np.gams2.business.bill.TransferBillDefine");//部门间资产调拨单据打印 
		funtionName_tableName_mapping.put("cardSplit", "com.jiuqi.np.gams2.business.bill.AssetsplitBillDefine");//卡片拆分单据打印
		funtionName_tableName_mapping.put("assetWithdraw", "com.jiuqi.np.gams2.business.bill.WithdrawBillDefine");//资产退库单据打印
		funtionName_tableName_mapping.put("handoverBill", "com.jiuqi.np.gams2.business.bill.HandoverBillDefine");//资产交接单据打印
		funtionName_tableName_mapping.put("reportOld", "com.jiuqi.np.gams2.business.bill.InnerdispregBillDefine");//校内处置报废单据打印
		funtionName_tableName_mapping.put("setting", "GAMS_PRINTING_SETTING");
	}
	
	/**
	 * 功能标识-功能名称映射
	 */
	public static Map<String, String> functionName_functionTitle_mapping = new HashMap<String, String>();
	static {
		functionName_functionTitle_mapping.put("checkIn", "登记建账单");
		functionName_functionTitle_mapping.put("exchange", "调剂单");
		functionName_functionTitle_mapping.put("commonChange", "普通信息变动单");
		functionName_functionTitle_mapping.put("importantChange", "增减值变动单");
		functionName_functionTitle_mapping.put("assetChange", "资产分类变动单");
		functionName_functionTitle_mapping.put("reportOld", "处置报废单");
		functionName_functionTitle_mapping.put("handoverBill", "资产交接单");
		functionName_functionTitle_mapping.put("innerTransfer", "部门内资产调拨单");
		functionName_functionTitle_mapping.put("transfer", "部门间资产调拨单");
		functionName_functionTitle_mapping.put("cardSplit", "卡片拆分单");
		functionName_functionTitle_mapping.put("assetWithdraw", "资产退库单");
		functionName_functionTitle_mapping.put("handoverBill", "资产交接单");
		functionName_functionTitle_mapping.put("setting", "日常运维");
	}
	
	/**
	 * 资产服务ip:端口号
	 */
	public static String zcglServiceIpAndPort;
	/**
	 * 单据打印机名称
	 */
	public static String billPrinterName;
	/**
	 * 条码打印机名称
	 */
	public static String barPrinterName;
	static {
		if(zcglServiceIpAndPort == null) {
			zcglServiceIpAndPort = PDFPrintUtil.getZcglServiceIpAndPortFromProperties();
		}
		if(billPrinterName == null) {
			billPrinterName = PDFPrintUtil.getPrinterName("bill");
		}
		if(barPrinterName == null) {
			barPrinterName = PDFPrintUtil.getPrinterName("label");
		}
	}
	
	/**
	 * 登录接口url
	 */
	public static String loginWebServiceUrl = "http://" + zcglServiceIpAndPort + "/terminalprint_xgd/login?wsdl";
	/**
	 * 卡片接口url
	 */
	public static String cardWebServiceUrl = "http://" + zcglServiceIpAndPort + "/terminalprint_xgd/PrintingTerminalAssetCard?wsdl";
	/**
	 * 单据接口url
	 */
	public static String billWebServiceUrl = "http://" + zcglServiceIpAndPort + "/terminalprint_xgd/PrintingTerminalBillCommon?wsdl";
	/**
	 * 打印机纸张设置接口
	 */
	public static String settingWebServiceUrl = "http://" + zcglServiceIpAndPort + "/terminalprint_xgd/PrintingTerminalSetting?wsdl";
	
	/**
	 * 终端管理接口
	 */
	public static String terminalManageWebserviceUrl = "http://" + zcglServiceIpAndPort + "/terminalprint_xgd/terminalManageWebservice?wsdl";
	
	/**
	 * 本地mac地址
	 */
	public static String MACADDRESS;
	
	/**
	 * 本地IP
	 */
	public static String IP;
	static {
		if(MACADDRESS == null) {
				try {
					MACADDRESS = PDFPrintUtil.getIntenetInfo().get("MACADDRESS");
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (SocketException e) {
					e.printStackTrace();
				}
		}
		if(IP == null) {
			try {
				IP = PDFPrintUtil.getIntenetInfo().get("IP");
			} catch (UnknownHostException | SocketException e) {
				e.printStackTrace();
			}
		}
	}
}
