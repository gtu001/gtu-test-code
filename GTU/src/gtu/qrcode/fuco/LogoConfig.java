package gtu.qrcode.fuco;

import java.awt.Color;

public class LogoConfig {
	public int border;//QRCode Logo 邊框框度
	public Color borderColor;//QRCode Logo 邊框顏色
	public int logoPart;//QRCode Logo 邊框大小 ，若為5則代表佔總圖1/5

	public LogoConfig(){
		
	}
	
	public int getBorder() {
		return border;
	}
	
	public void setBorder(int border) {
		this.border = border;
	}
	
	public Color getBorderColor() {
		return borderColor;
	}
	
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}
	
	public int getLogoPart() {
		return logoPart;
	}
	
	public void setLogoPart(int logoPart) {
		this.logoPart = logoPart;
	}

}
