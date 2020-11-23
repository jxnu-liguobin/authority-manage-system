package cn.edu.jxnu.base.exception;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常
 *
 * @author 梦境迷离
 * @version V1.0
 */
@Controller
public class NotFoundExceptionImpl implements ErrorController {
    private static final String ERROR_PATH = "/error";

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    /**
     * 404 500 处理
     *
     * @param request
     * @param response
     * @return ModelAndView
     */
    @RequestMapping(value = ERROR_PATH)
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mView = new ModelAndView();
        if (response.getStatus() == 404) {
            mView.setViewName("/assets/404.html");
        }
        if (response.getStatus() == 500) {
            mView.setViewName("/assets/500.html");
        }
        return mView;
    }

}
