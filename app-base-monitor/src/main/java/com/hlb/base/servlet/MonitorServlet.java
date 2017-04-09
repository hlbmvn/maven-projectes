package com.hlb.base.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.hlb.base.monitor.model.CPUModel;
import com.hlb.base.monitor.model.FileModel;
import com.hlb.utils.date.DateUtil;
import com.hlb.utils.image.VerifyCodeUtils;

/**
 * 用于获取监控资源的Servlet
 */
public class MonitorServlet extends HttpServlet {
	
	private static Logger log = LoggerFactory.getLogger(MonitorServlet.class);
	
	private static final int maxCnt = 10;
	private static final long serialVersionUID = 1L;
	//页面访问量 实时和半小时的
	private static List<Map<Long, Integer>> pv1 = new LinkedList<Map<Long,Integer>>();
	private static List<Map<Long, Integer>> pv2 = new LinkedList<Map<Long,Integer>>();
	private static long time = System.currentTimeMillis();
	private static long halfHour = 0;
	// CPU 模型
	private static Sigar sigar = new Sigar();
	private static CPUModel cpuModel = new CPUModel();
	//磁盘大小
	private static List<FileModel> files = new ArrayList<FileModel>();
	
	static{
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = calendar.get(Calendar.MINUTE);
		String dateStr = DateUtil.getCurrentDate(DateUtil.DATE_FORMAT_8);
		if(minutes > 30){
			hour = hour+1;
			if(hour < 10){
				dateStr = dateStr+"0"+hour+"0000";
			}else{
				dateStr = dateStr+hour+"0000";
			}
		}else{
			minutes = 30;
			if(hour < 10){
				dateStr = dateStr+"0"+hour+"3000";
			}else{
				dateStr = dateStr+hour+"3000";
			}
		}
		try {
			halfHour = DateUtil.getDateFromString(dateStr, DateUtil.DATE_FORMAT_14).getTime();
			log.debug("获取当前半小时的时间为:[{}]",halfHour);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Map<Long, Integer> tmp = new HashMap<Long, Integer>();
		tmp.put(time, 0);
		pv1.add(tmp);
		Map<Long, Integer> tmp1 = new HashMap<Long, Integer>();
		tmp1.put(halfHour, 0);
		pv2.add(tmp1);
		try {
			sigar.getCpuPercList();
		} catch (SigarException e) {
			e.printStackTrace();
		}
	}
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MonitorServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		String res = "";
		Gson gson = new Gson();
		if("01".equals(type)){
			res = gson.toJson(pv1); 
		}else if("02".equals(type)){
			res = gson.toJson(pv2); 
		}else if("03".equals(type)){
			try {
				CpuPerc cpuList[] = sigar.getCpuPercList();
				cpuModel.setCombined(0.0);
				cpuModel.setCpus(new ArrayList<Double>());
				Double combined = 0.0;
		        for (int i = 0; i < cpuList.length; i++) {// 不管是单块CPU还是多CPU都适用
		        	CpuPerc cpuPerc = cpuList[i];
		        	combined =combined + cpuPerc.getCombined()*10000;
		        	cpuModel.getCpus().add(Math.round(cpuPerc.getCombined()*10000)/100.0);
		        }
		        cpuModel.setCombined(Math.round(combined/cpuList.length)/100.0);
			} catch (SigarException e) {
				e.printStackTrace();
			}
			res = gson.toJson(cpuModel);
		}else if("04".equals(type)){
			try {
				files.clear();
				long time = System.currentTimeMillis();
				FileSystem fslist[] = sigar.getFileSystemList();
				for(FileSystem fs:fslist){
					if(fs.getType()==2){
						FileModel file = new FileModel();
						FileSystemUsage usage = null;
			            usage = sigar.getFileSystemUsage(fs.getDirName());
			            file.setFileName(fs.getDevName());
			            file.setTotal(usage.getTotal());
			            file.setUsedCnt(usage.getUsed());
			            file.setTime(time);
			            files.add(file);
					}
				}
			} catch (SigarException e) {
				e.printStackTrace();
			}
			res = gson.toJson(files);
		}else if("05".equals(type)){
			Runtime runTime = Runtime.getRuntime();
			Map<String,Long> map = new HashMap<String, Long>();	
			map.put("totalMemory", runTime.totalMemory());
			map.put("maxMemory", runTime.maxMemory());
			map.put("freeMemory", runTime.freeMemory());
			map.put("time", System.currentTimeMillis());
			res = gson.toJson(map);
		}else if("06".equals(type)){
			try {
				 // 当前内存剩余量
		        Swap swap = sigar.getSwap();
		        Mem mem = sigar.getMem();
		        Map<String,Long> map = new HashMap<String, Long>();	
				map.put("Total", mem.getTotal());// 内存总量
				map.put("Used", mem.getUsed()); // 当前内存使用量
				map.put("Free", mem.getFree());
				map.put("time", System.currentTimeMillis());
				map.put("swap.Total", swap.getTotal());
				map.put("swap.Used", swap.getUsed());
				map.put("swap.Free", swap.getFree());
				res = gson.toJson(map);
			} catch (SigarException e) {
				e.printStackTrace();
			}
		}else if("07".equals(type)){
			response.setContentType("application/octet-stream");
			String code = VerifyCodeUtils.generateVerifyCode(4);
			System.out.println(code);
			VerifyCodeUtils.genVerifyImage(100, 40, response.getOutputStream(), code);
			return ;
		}
		response.getWriter().write(res);;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	public synchronized static void add(){
		long cur = System.currentTimeMillis();
		//实时PV的计算
		if((cur-time) > 1000){
			if(pv1.size() > maxCnt){
				pv1.remove(0);
			}
			Map<Long, Integer> tmp = new HashMap<Long, Integer>();
			tmp.put(cur, 1);
			pv1.add(tmp);
			time = cur;
		}else{
			Map<Long, Integer> tmp = pv1.get(pv1.size()-1);
			tmp.put(time, tmp.get(time)+1);
		}
		
		//半小时PV的计算
		if(cur>halfHour){
			nextHalfHour();
			if(pv2.size() > maxCnt){
				pv2.remove(0);
			}
			Map<Long, Integer> tmp = new HashMap<Long, Integer>();
			tmp.put(halfHour, 1);
			pv2.add(tmp);
		}else{
			Map<Long, Integer> tmp = pv2.get(pv2.size()-1);
			tmp.put(halfHour, tmp.get(halfHour)+1);
		}
	}

	public static void nextHalfHour(){
		long currentTime = System.currentTimeMillis();
		long split = 30*60*1000;
		long sty = (currentTime - halfHour)/split;
		halfHour =  halfHour + (split*sty + split);
	}	
}
