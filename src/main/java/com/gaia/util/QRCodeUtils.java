package com.gaia.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.codec.Charsets;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONObject;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class QRCodeUtils {

	private static String filePath = "/usr/share/nginx/html/img/";

	private static String affix = "http://106.75.209.173/img/";

	/**
	 * 生成二维码
	 *
	 * @param outputStream
	 * @param content
	 * @param qrCodeSize
	 * @param imageFormat
	 */
	public static String createQrCode(OutputStream outputStream, String content, int qrCodeSize, String imageFormat) throws IOException {
		try {
			// Create the ByteMatrix for the QR-Code that encodes the given String.
			//Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType,
			//		ErrorCorrectionLevel>();
			Hashtable<EncodeHintType, Object> hintMap = new Hashtable<>();
			hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
			hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix byteMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hintMap);

			// Make the BufferedImage that are to hold the QRCode
			int matrixWidth = byteMatrix.getWidth();
			BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
			image.createGraphics();
			Graphics2D graphics = (Graphics2D) image.getGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, matrixWidth, matrixWidth);

			// Paint and save the image using the ByteMatrix
			graphics.setColor(Color.BLACK);
			for (int i = 0; i < matrixWidth; i++) {
				for (int j = 0; j < matrixWidth; j++) {
					if (byteMatrix.get(i, j)) {
						graphics.fillRect(i, j, 1, 1);
					}
				}
			}
            log.info(filePath);
			String fileName =  System.currentTimeMillis()+".jpg";
			outputStream = new FileOutputStream(filePath+ fileName);
			ImageIO.write(image, imageFormat, outputStream);
			return affix + fileName;
		} catch (Exception ex) {
			log.info("qrcode:"+ ex.getMessage());
		}
		return null;
	}

	public static String sendHttpPost(String url, String body) throws Exception {
		 CloseableHttpClient httpClient = HttpClients.createDefault();
		 HttpPost httpPost = new HttpPost(url);
		 httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
		 httpPost.setHeader("Accept", "application/json");
		 httpPost.setEntity(new StringEntity(body, Charsets.UTF_8));
		 CloseableHttpResponse response = httpClient.execute(httpPost);
		 HttpEntity entity = response.getEntity();
		 String responseContent = EntityUtils.toString(entity, "UTF-8");
		 response.close();
		 httpClient.close();
		 return responseContent;
	}

	public static  String createMiniCode(Map<String,String> param) throws Exception {
		log.info("======生成微信小程序码开始====="+param);
		long nowTime = System.currentTimeMillis();
//		String encoder =
//				URLEncoder.encode("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + param.get(
//						"accessToken"), Charsets.UTF_8.name());
		URL getCodeUrl =
				new URL("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + param.get(
		"accessToken"));
		HttpURLConnection httpURLConnection = (HttpURLConnection) getCodeUrl.openConnection();
		httpURLConnection.setRequestMethod("POST");// 提交模式
		httpURLConnection.setRequestProperty("Content-Type","application/json;charset=UTF-8");
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setDoInput(true);
		PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
		JSONObject json = new JSONObject();
		json.put("scene", param.get("scene"));
		json.put("page",param.get("page"));
		json.put("width", 200);
		json.put("auto_color", false);
		JSONObject lineColor = new JSONObject();
		lineColor.put("r", 0);
		lineColor.put("g", 0);
		lineColor.put("b", 0);
		json.put("line_color", lineColor);
		printWriter.write(json.toString());
		printWriter.flush();
		BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
		String fileName = System.currentTimeMillis() + ".png";
		File file = new File(filePath + fileName);
		FileOutputStream os = new FileOutputStream(file);
//		file.createNewFile();
//		InputStream is = httpURLConnection.getInputStream();
//		Files.copy(is,file.toPath());
		//is.close();

		int len;
		byte[] arr = new byte[1024];
		while ((len = bis.read(arr)) != -1) {
			os.write(arr, 0, len);
			os.flush();
		}
		os.close();
		log.info("========生成微信小程序码结束===========");
		log.info(affix+fileName);
		return affix+fileName;
	}

	public static String createQrCode(OutputStream outputStream,String content){
		try {
            log.info(".................."+content);
			int width = 200; // 图像宽度
			int height = 200; // 图像高度
			String format = "jpg";// 图像类型
			Map<EncodeHintType, Object> hints = new HashMap<>();
			//内容编码格式
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			// 指定纠错等级
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			//设置二维码边的空度，非负数
			hints.put(EncodeHintType.MARGIN, 1);
			BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
			String fileName = System.currentTimeMillis()+".png";
			MatrixToImageWriter.writeToPath(bitMatrix, format, new File(filePath+fileName).toPath());// 输出原图片
//			MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(0xFF000001, 0xFFFFFFFF);
//
//			BufferedImage bufferedImage = LogoMatrix(MatrixToImageWriter.toBufferedImage(bitMatrix,
//					matrixToImageConfig), new File("d:/uploads/1582358254212222.jpg"));
//			outputStream = new FileOutputStream(filePath+ fileName);
//			ImageIO.write(bufferedImage, "png", outputStream);
			return affix + fileName;
		} catch (Exception e) {
			log.error(  " 二维码生成错误！", e);
		}
		return null;
	}

	public static BufferedImage LogoMatrix(BufferedImage matrixImage, File logoFile) throws IOException{
		/**
		 * 读取二维码图片，并构建绘图对象
		 */
		Graphics2D g2 = matrixImage.createGraphics();

		int matrixWidth = matrixImage.getWidth();
		int matrixHeigh = matrixImage.getHeight();

		/**
		 * 读取Logo图片
		 */
		BufferedImage logo = ImageIO.read(logoFile);

		//开始绘制图片
		g2.drawImage(logo,matrixWidth/5*2,matrixHeigh/5*2, matrixWidth/5, matrixHeigh/5, null);//绘制
		BasicStroke stroke = new BasicStroke(5,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
		g2.setStroke(stroke);// 设置笔画对象
		//指定弧度的圆角矩形
		RoundRectangle2D.Float round = new RoundRectangle2D.Float(matrixWidth/5*2, matrixHeigh/5*2, matrixWidth/5, matrixHeigh/5,20,20);
		g2.setColor(Color.white);
		g2.draw(round);// 绘制圆弧矩形

		//设置logo 有一道灰色边框
		BasicStroke stroke2 = new BasicStroke(1, BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
		g2.setStroke(stroke2);// 设置笔画对象
		RoundRectangle2D.Float round2 = new RoundRectangle2D.Float(matrixWidth/5*2+2, matrixHeigh/5*2+2, matrixWidth/5-4, matrixHeigh/5-4,20,20);
		g2.setColor(new Color(128,128,128));
		g2.draw(round2);// 绘制圆弧矩形

		g2.dispose();
		matrixImage.flush() ;
		return matrixImage ;
	}


	/**
	 * 读取二维码
	 *
	 * @param inputStream
	 * @throws IOException
	 */
	public static String readQrCode(InputStream inputStream) throws IOException {

		// get the data from the input stream
		BufferedImage image = ImageIO.read(inputStream);

		// convert the image to a binary bitmap source
		LuminanceSource source = new BufferedImageLuminanceSource(image);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

		// decode the barcode
		QRCodeReader reader = new QRCodeReader();

		Result result = null;
		try {
			result = reader.decode(bitmap);
		} catch (ReaderException e) {
			// the data is improperly formatted
			e.printStackTrace();
		}

		log.info(result.getText());
		return result.getText();
	}
}
