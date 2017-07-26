package com.hlb.utils.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketUtils {
	/**
	 * 获取链接
	 */
	private static Socket getConnection(String ip, int port) throws Exception {
		Socket socket = new Socket(ip, port);
		return socket;
	}

	/**
	 * SOCKET进行数据发送
	 */
	public static String sendData(Object obj, SocketCfg cfg) {
		// 获取链接
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		try {
			// 获取sockte连接
			socket = getConnection(cfg.getIp(), cfg.getPort());
			socket.setSoTimeout(cfg.getTimeOut());

			// 得到输出流进行数据发送
			out = socket.getOutputStream();
			byte[] datas = null;
			if (obj instanceof byte[]) {
				datas = (byte[]) obj;
			} else {
				datas = obj.toString().getBytes(cfg.getEncoding());
			}
			out.write(datas);

			// 得到输入流，获取回复数据
			in = socket.getInputStream();
			byte[] buf = new byte[cfg.getBufLen()];
			int len = in.read(buf);
			String returnStr = new String(buf, 0, len, cfg.getEncoding());

			return returnStr;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭连接
			try {
				out.close();
				in.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
