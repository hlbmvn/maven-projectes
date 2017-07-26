package com.hlb.utils.zxing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.EnumMap;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 二维码生成工具，使用Google的Zxing
 * */
public class ZXUtil {
	
    private static final int width = 300;  
    private static final int height = 300;
    
    //二维码格式参数  
    private static final EnumMap<EncodeHintType, Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);  
    static{  
        /*二维码的纠错级别(排错率),4个级别： 
         L (7%)、 
         M (15%)、 
         Q (25%)、 
         H (30%)(最高H) 
         纠错信息同样存储在二维码中，纠错级别越高，纠错信息占用的空间越多，那么能存储的有用讯息就越少；共有四级； 
         选择M，扫描速度快。 
         */  
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);  
        hints.put(EncodeHintType.MARGIN, 1);  
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");   
    }
    
    /** 
     * 绘制二维码 
     * @param contents 二维码内容   
     * @return image 二维码图片 
     * @throws WriterException 
     * */  
    private static BufferedImage encodeImg(String contents) throws WriterException{  
    	BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,  
                BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵
    	return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }  
    
    /**
     * 合并图层
     * 如logo的叠加
     * @param zxingCode 二维码 
     * @param logoImg logo图片
     * */
    private static BufferedImage encodeImg(String contents,BufferedImage logoImg) throws WriterException{  
    	BufferedImage zxingCode = encodeImg(contents);
    	//获取画笔  
        Graphics2D g = zxingCode.createGraphics();  
        //设置二维码大小，太大，会覆盖二维码，此处20%  
        int logoWidth = logoImg.getWidth(null) > zxingCode.getWidth()*2 /10 ? (zxingCode.getWidth()*2 /10) : logoImg.getWidth(null);  
        int logoHeight = logoImg.getHeight(null) > zxingCode.getHeight()*2 /10 ? (zxingCode.getHeight()*2 /10) : logoImg.getHeight(null);  
        //中心  
        int x = (zxingCode.getWidth() - logoWidth) / 2;  
        int y = (zxingCode.getHeight() - logoHeight) / 2;  
        //开始合并绘制图片  
        g.drawImage(logoImg, x, y, logoWidth, logoHeight, null);  
        g.drawRoundRect(x, y, logoWidth, logoHeight, 15 ,15);  
        g.setStroke(new BasicStroke(2));  
        //logo边框颜色  
        g.setColor(Color.WHITE);  
        g.drawRect(x, y, logoWidth, logoHeight);  
        g.dispose();  
        zxingCode.flush();  
    	return zxingCode;
    }
    
    
    /** 
     * 二维码输出到文件 
     *  @param contents 二维码内容 
     * @param format 图片格式 
     * @param file 输出文件 
     * @throws IOException 
     * @throws WriterException 
     * */  
    public static void writeToFile(String contents,String format,File file) throws IOException, WriterException{  
        BufferedImage image = encodeImg(contents);  
        ImageIO.write(image, format, file);  
    }
    
    /** 
     * 二维码流式输出 
     *  @param contents 二维码内容 
     * @param format 图片格式 
     * @param stream 输出流 
     * @throws IOException 
     * @throws WriterException 
     * */  
    public static void writeToStream(String contents,String format,OutputStream stream) throws IOException, WriterException{  
        BufferedImage image = encodeImg(contents);  
        ImageIO.write(image, format, stream);  
    }
    
    /** 
     * 二维码输出到文件 
     *  @param contents 二维码内容 
     * @param format 图片格式 
     * @param file 输出文件 
     * @throws IOException 
     * @throws WriterException 
     * */  
    public static void writeToFile(String contents,String format,File logoImg,File file) throws IOException, WriterException{  
        BufferedImage image = encodeImg(contents,ImageIO.read(logoImg)); 
        
        ImageIO.write(image, format, file);  
    }
    
    /** 
     * 二维码流式输出 
     *  @param contents 二维码内容 
     * @param format 图片格式 
     * @param stream 输出流 
     * @throws IOException 
     * @throws WriterException 
     * */  
    public static void writeToStream(String contents,String format,File logoImg,OutputStream stream) throws IOException, WriterException{  
    	BufferedImage image = encodeImg(contents,ImageIO.read(logoImg));  
        ImageIO.write(image, format, stream);  
    }

}
