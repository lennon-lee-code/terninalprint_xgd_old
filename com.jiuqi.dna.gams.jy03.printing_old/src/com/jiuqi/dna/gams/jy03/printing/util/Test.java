package com.jiuqi.dna.gams.jy03.printing.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;

public class Test {
  public static void main(String[] args) throws UnknownHostException, IOException {
	  InetAddress ip = InetAddress.getLocalHost();
	  System.out.println("IP:"+ip);
	  NetworkInterface network = NetworkInterface.getByInetAddress(ip);
	  byte[] mac = network.getHardwareAddress();
	  StringBuilder sb = new StringBuilder();
	  for (int i = 0; i < mac.length; i++) {
	  sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
	  }
	  String hostMac=sb.toString();
	  String[] newIP = ip.toString().split("/");
	  System.out.println("newIP : "+newIP[1]);
	  System.out.println("Current MAC address : "+hostMac);
  }
}
