package gtu.qrcode.fuco;

import java.awt.Color;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;

public class QRCodeConfig {
	public static String PicType_PNG = "png";
	public static String PicType_JPEG = "jpg";
	public static String PicType_GIF = "gif";
	
	private boolean logoFlg = false;//QRCode是否包含Logo小圖
    private String content;//QRCode內容
    private int width = 360;//QRCode寬度
    private int height = 360;//QRCode高度
    private Map<EncodeHintType, ?> hints;   // 设置参数
    private String logoPath;//Logo圖片位置
    private String putPath;//圖片輸出路徑
    private LogoConfig LogoConfig;//Logo圖參數
    private String picType;
    private Color color4Code = Color.BLACK;//產生QRCode的紋路顏色
    private Color color4Back = Color.WHITE;//產生QRCode的底色
    
	public boolean isLogoFlg() {
		return logoFlg;
	}
	public void setLogoFlg(boolean logoFlg) {
		this.logoFlg = logoFlg;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public Map<EncodeHintType, ?> getHints() {
		return hints;
	}
	public void setHints(Map<EncodeHintType, ?> hints) {
		this.hints = hints;
	}
	public String getLogoPath() {
		return logoPath;
	}
	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}
	public String getPutPath() {
		return putPath;
	}
	public void setPutPath(String putPath) {
		this.putPath = putPath;
	}
	public LogoConfig getLogoConfig() {
		return LogoConfig;
	}
	public void setLogoConfig(LogoConfig logoConfig) {
		LogoConfig = logoConfig;
	}
	public String getPicType() {
		return picType;
	}
	public void setPicType(String picType) {
		this.picType = picType;
	}
	public Color getColor4Code() {
		return color4Code;
	}
	public void setColor4Code(Color color4Code) {
		this.color4Code = color4Code;
	}
	public Color getColor4Back() {
		return color4Back;
	}
	public void setColor4Back(Color color4Back) {
		this.color4Back = color4Back;
	}
}
