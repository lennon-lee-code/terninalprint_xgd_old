package com.jiuqi.dna.gams.jy03.printing.util;

import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.print.PrintService;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;

import com.itextpdf.text.log.SysoCounter;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

import net.sf.json.JSONObject;

/**
 * 自助打印终端打印工具类
 * @author wangjiao01
 *
 */
public class PDFPrintUtil {
	
	public static Properties config;
	static {
		if(config == null) {
			config = loadConfig();
		}
	}
	
	/**
	 * 获取临时生成的pdf文件路径
	 * @param pdfData
	 * @return
	 */
	public static String getNewPDFPath(byte[] pdfData,int index) {
		
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String newPdfName = df.format(new Date());
		String newPdfPath = "D:\\np_xgd2\\printpdf\\" + newPdfName+index+ ".pdf";// 随具体环境变化
		
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(newPdfPath);
			outputStream.write(pdfData);
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			if(outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return newPdfPath;
	}
	
	/**
	 * 执行打印
	 * @param pdfData pdf文档对应的二进制数组
	 * @param printerName 打印机标识
	 * @param copyCount 打印份数
	 * @return
	 * @throws IOException
	 */
	public static JSONObject doPrintByPDFBox(byte[] pdfData, String printerName, Integer copyCount) throws IOException {
		JSONObject result = new JSONObject();
		PDDocument document = null;
		try {
			document = PDDocument.load(pdfData);
			PrinterJob printerJob = PrinterJob.getPrinterJob();
			
			// 查找并设置打印机
			PrintService[] printServices = PrinterJob.lookupPrintServices();
			if(printServices == null || printServices.length == 0) {
				result = getPrintMessage(false, "打印失败，计算机未安装打印机，请检查。");
//				makeSound("打印失败，计算机未安装打印机，请检查。");
				return result;
			}
			PrintService printService = null;
			for(int i = 0; i < printServices.length; i++) {
				if(printServices[i].getName().equalsIgnoreCase(printerName)) {
					System.out.println(printServices[i].getName());
					printService = printServices[i];
					break;
				}
			}
			if(printService != null) {
				printerJob.setPrintService(printService);
			} else {
				result = getPrintMessage(false, "打印失败，未找到名称为" + printerName + "的打印机，请检查。");
//				makeSound("打印失败，未找到名称为" + printerName + "的打印机，请检查。");
				return result;
			}
			
			// 设置纸张
			PDFPrintable pdfPrintable = new PDFPrintable(document, Scaling.ACTUAL_SIZE);
			PageFormat pageFormat = new PageFormat();
			pageFormat.setOrientation(PageFormat.PORTRAIT);
			pageFormat.setPaper(getPaper(printerName));
			// Book 的方式实现打印多张（已测试，可行）
			Book book = new Book();
			book.append(pdfPrintable, pageFormat, document.getNumberOfPages());
			printerJob.setPageable(book);
			// PDFPageable 的方式实现打印多张（未测试，预测可行）
//			PDFPageable pdfPageable = new PDFPageable(document);
//			pdfPageable.append(pdfPrintable, pageFormat, document.getNumberOfPages());
//			printerJob.setPageable(pdfPageable);
			
			// 测试
			System.out.println(document.getNumberOfPages());
			System.out.println(book.getNumberOfPages());
//			System.out.println(pdfPageable.getNumberOfPages());
			
			// 执行打印
			printerJob.setCopies(copyCount);
			printerJob.print();
			result = getPrintMessage(true, "打印成功。");
//			makeSound("打印成功，请取件。");
		} catch (Exception e) {
			e.printStackTrace();
			result = getPrintMessage(false, "打印失败：发生异常。");
//			makeSound("打印失败，打印时发生异常，请检查。");
		} finally {
			if(document != null) {
				document.close();// 起初文件删除失败，关闭文档之后，删除成功
			}
		}
		
		return result;
	}

	/**
	 * 获取打印结果信息，成功或失败，用以返回前台界面
	 * @param isPrintSuccess
	 * @param message
	 * @return
	 */
	public static JSONObject getPrintMessage(boolean isPrintSuccess, String message) {
	JSONObject jsonObject = new JSONObject();
		if(isPrintSuccess) {
			jsonObject.put("code", 1);
		}else {
			jsonObject.put("code", 0);
		}
		jsonObject.put("message", message);
		return jsonObject;
	}
	
	/**
	 * 删除打印过程中创建的临时pdf文件
	 * @param newPdfPath
	 * @return
	 */
	public static boolean deleteFile(String newPdfPath) {
		File file = new File(newPdfPath);
		if(file.exists()) {
			if(file.isFile()) {
				return file.delete();
			}
		}else {
			System.out.println("文件 " + newPdfPath + " 不存在！");
		}
		return false;
	}
	
	/**
	 * 打印语音提示：成功或失败，并提示失败原因
	 * @param message
	 */
	public static void makeSound(String message) {
		ActiveXComponent sap = new ActiveXComponent("Sapi.SpVoice");
		try {
			// 音量 0-100
			sap.setProperty("Volume", new Variant(100));
			// 语音朗读速度 -10 到 +10
			sap.setProperty("Rate", new Variant(0));
			// 获取执行对象
			Dispatch sapo = sap.getObject();
			// 执行朗读
			Dispatch.call(sapo, "Speak", new Variant(message));
			// 关闭执行对象
			sapo.safeRelease();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭应用程序连接
			sap.safeRelease();
		}
	}
	
		
	
	/**
	 * 根据打印机名称判断是单据打印还是条码打印，进而创建对应Paper对象并返回
	 * @param printerName
	 * @return
	 */
	public static Paper getPaper(String printerName) {
		Paper paper = new Paper();
		// 默认为A4纸张，对应像素宽和高分别为 595, 848
		int width = 595;
		int height = 848;
		// 设置边距，单位是像素，10mm边距，对应 28px
		int marginLeft = 10;
		int marginRight = 0;
		int marginTop = 10;
		int marginBottom = 0;
		// 如果是条码打印
		if(printerName.equalsIgnoreCase(config.getProperty("barPrinterName"))) {
			// 云南大学条码纸张规格70mm宽*40mm高，对应像素值为 198, 113
			width = getPaperSize("width");
			height = getPaperSize("height");
		}
		paper.setSize(width, height);
		// 下面一行代码，解决了打印内容为空的问题
		paper.setImageableArea(marginLeft, marginRight, width - (marginLeft + marginRight), height - (marginTop + marginBottom));
		return paper;
	}

	/**
	 * 加载配置文件
	 * @return
	 */
	public static Properties loadConfig() {
		Properties properties = new Properties();
		String path = PDFPrintUtil.class.getResource("/").getPath();
		path = path.substring(1, path.indexOf("WEB-INF"));
		File file = new File(path + "file/config.properties");
		InputStream ins = null;
		try {
			ins = new FileInputStream(file);
			properties.load(ins);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			if(ins != null) {
				try {
					ins.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return properties;
	}
	
	/**
	 * 从配置文件中读取资产管理服务的ip和端口号
	 * @return
	 */
	public static String getZcglServiceIpAndPortFromProperties() {
		if(config == null) {
			loadConfig();
		}
		return config.getProperty("zcgl_service_ip") + ":" + config.getProperty("zcgl_service_port");
	}
	
	/**
	 * 从配置文件中读取单据打印机或条码打印机的标识
	 * @param printType
	 * @return
	 */
	public static String getPrinterName(String printType) {
		if(config == null) {
			config = loadConfig();
		}
		return printType.equalsIgnoreCase("bill") ? config.getProperty("billPrinterName") : config.getProperty("barPrinterName");
	}
	
	/**
	 * 从配置文件中读取条码打印纸张的宽和高（单位：pix）
	 * @param type
	 * @return
	 */
	public static Integer getPaperSize(String type) {
		if(config == null) {
			config = loadConfig();
		}
		
		// 单位：mm
		int width = Integer.valueOf(config.getProperty("paperWidth"));
		int height = Integer.valueOf(config.getProperty("paperHeight"));
		
		// 单位：pix
		width = (int) (width / 25.4 * 72);
		height = (int) (height / 25.4 * 72);
		
		return type.equalsIgnoreCase("width") ? width : height;
	}
	
	public static String getChineseChangeType(String changeType) {
		String chineseChangeType = null;
		switch (changeType) {
		case "label":
			chineseChangeType = "标签";
			break;
		case "carbon":
			chineseChangeType = "碳带";
			break;
		case "cartridge":
			chineseChangeType = "硒鼓";
			break;
		case "bill":
			chineseChangeType = "A4纸";
			break;
		default:
			chineseChangeType = "未知类型";
			break;
		}
		return chineseChangeType;
	}
	
	public static Map<String,String> getIntenetInfo() throws UnknownHostException, SocketException{
		  Map<String,String> result = new HashMap<String,String>(); 
		  InetAddress ip = InetAddress.getLocalHost();
		  NetworkInterface network = NetworkInterface.getByInetAddress(ip);
		  byte[] mac = network.getHardwareAddress();
		  StringBuilder sb = new StringBuilder();
		  for (int i = 0; i < mac.length; i++) {
		  sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
		  }
		  String[] newIP = ip.toString().split("/");
		  String hostMac=sb.toString();
		  result.put("MACADDRESS", hostMac);
		  result.put("IP", newIP[1]);
		  return result;
	}
	
}
