package core.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * 验证码生成工具类
 * @author dev-teng
 * @date 2018年2月8日
 */
public class ValidationCodeUtil {
		// 验证码中的候选字符
		private static char[] chars = "1234567890abcdefghijklmnopqrstuvwxyz".toCharArray();

		/**
		 * 生成验证码的一个工具类
		 * 返回随机抽取的4字符。
		 * @param outputStream
		 * @throws IOException 
		 */
		public static String createValidationCodeImage(OutputStream outputStream) {

			// 创建一个新的图片对象，但现在图片中没有内容
			BufferedImage image = new BufferedImage(80, 35, BufferedImage.TYPE_INT_BGR);

			// 在图片上画上内容
			// 1, 得到画笔，再去画。
			Graphics graphics = image.getGraphics();
			// 2, 把背景颜色改为白色
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
			Random random = new Random();

			// 随机抽出来4个字符
			String code = "";
			for (int i = 0; i < 4; i++) {
				int index = random.nextInt(chars.length);
				code += chars[index];
			}
			// System.out.println("---> chars = " + str);

			// 
			graphics.setColor(Color.GRAY);
			graphics.setFont(new Font("黑体", Font.BOLD, 28));
			graphics.drawString(code, 10, 25);

			// 4, 生成一些干扰的图形
			for (int i = 0; i < 50; i++) {
				int x = random.nextInt(image.getWidth());
				int y = random.nextInt(image.getHeight());
				int width = 1 + random.nextInt(2);
				int height = 1 + random.nextInt(2);

				int index = random.nextInt(4);
				Color[] colors = { Color.GRAY, Color.BLACK, Color.GRAY, Color.LIGHT_GRAY };
				graphics.setColor(colors[index]);

				graphics.drawOval(x, y, width, height); // 画圆形
			}

			// 保存图片
			// ImageIO.write(image, "png", new File("d:/aa.png"));
			// 输出图片
			try {
				ImageIO.write(image, "png", outputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					if(null!=outputStream)
						outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return code;
		}
		
}
