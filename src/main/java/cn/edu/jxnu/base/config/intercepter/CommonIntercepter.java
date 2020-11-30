/* 梦境迷离 (C)2020 */
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
 * @version V2.0 2020年11月20日
 */
@Component
public class CommonIntercepter implements HandlerInterceptor {

  /**
   * 返回true
   *
   * @param request request
   * @param response response
   * @param handler handler
   * @return true
   */
  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) {
    return true;
  }

  /**
   * 注册全局变量作为前台应用路径
   *
   * @param request request
   * @param response response
   * @param handler handler
   * @param modelAndView modelAndView
   */
  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      ModelAndView modelAndView) {
    request.setAttribute("ctx", request.getContextPath());
  }

  /**
   * 请求之后需要处理的
   *
   * @param request request
   * @param response response
   * @param handler handler
   * @param ex 异常
   */
  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {}
}
