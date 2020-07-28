package cn.edu.jxnu.base.config.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 公共拦截器
 * 
 * @author 梦境迷离
 * @time 2018年4月10日 下午5:03:28
 * @version V1.0
 */
@Component
public class CommonIntercepter implements HandlerInterceptor {

	/**
	 * 返回true
	 * 
	 * @time 2018年4月10日 下午5:03:36
	 * @version V1.0
	 * @param request
	 * @param response
	 * @param handler
	 * @return true
	 * @throws Exception
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		return true;
	}

	/**
	 * 注册全局变量作为前台应用路径
	 * 
	 * @time 2018年4月10日 下午5:04:00
	 * @version V1.0
	 * @param request
	 * @param response
	 * @param handler
	 * @param modelAndView
	 * @throws Exception
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		request.setAttribute("ctx", request.getContextPath());
	}

	/**
	 * 请求之后需要处理的
	 * 
	 * @time 2018年4月10日 下午5:04:49
	 * @version V1.0
	 * @param request
	 * @param response
	 * @param handler
	 * @param ex
	 * @throws Exception
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
