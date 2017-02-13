package com.hlb.base.monitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hlb.base.monitor.model.Log;
import com.hlb.base.monitor.model.LogCfg;
import com.hlb.base.servlet.MonitorServlet;
import com.hlb.utils.date.DateUtil;

/**
 * WEB 资源监控的类
 * （1）资源URL请求时间监控
 * （2）资源URL请求日志记载
 */
public class ResourceMonitorFilter implements Filter {
	
	private Logger log = LoggerFactory.getLogger(ResourceMonitorFilter.class); 
	
	//被检查的资源集
	private List<String> checkedUrls = new ArrayList<String>();
	//日志登记URL集合
	private List<String> logCheckedUrl = new ArrayList<String>();
	//日志登记配置
	private Map<String, LogCfg> logCfgs = new HashMap<String, LogCfg>();
	
    /**
     * Default constructor. 
     */
    public ResourceMonitorFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		
		MonitorServlet.add();
		
		//进行资源url的检查是否需要进行监测
		String url = request.getRequestURI();
		log.debug("资源的URL为[{}]",url);
		long start  = 0 ;
		if(isCheckUrl(url) || isLogCheckUrl(url)){
			start = System.currentTimeMillis();
			
		}
		chain.doFilter(request, response);
		if(start>0){
			int cost = (int) (System.currentTimeMillis() - start);
			log.debug("该URL请求共计耗时[{}]",cost);
			//(1) 进行资源URL响应效率登记
			if(isCheckUrl(url)){
				Log log = new Log();
				log.setType("00");
				log.setContent(url);
				log.setCostTime(cost);
				log.setCrtTime(DateUtil.getCurrentDate(DateUtil.DATE_FORMAT_14));
				log.setOprId("-");
				log.setRspMsg("-");
			}
			//(2) 进行日志的登记
			if(isLogCheckUrl(url)){
				Log log = new Log();
				log.setType("00");
				log.setContent(url);
				log.setCostTime(cost);
				log.setCrtTime(DateUtil.getCurrentDate(DateUtil.DATE_FORMAT_14));
				log.setOprId("-");
				log.setRspMsg("-");
				String context = "未找到指定URL[]的配置信息";
				String rspMgs  = "-1";
				if(logCfgs.get(url)!=null){
					
					
				}
			}
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
		//对参数配置进行初始化包括，checkedUrls logCheckedUrl logCfgs
		
		
	}
	
	private boolean isCheckUrl(String url){
		return checkedUrls.contains(url);
	}
	
	private boolean isLogCheckUrl(String url){
		return logCheckedUrl.contains(url);
	}
	
	
}
