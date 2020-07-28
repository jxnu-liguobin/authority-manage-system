//package cn.edu.jxnu.base.controller.admin.system;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import cn.edu.jxnu.base.common.JsonResult;
//import cn.edu.jxnu.base.controller.BaseController;
//import cn.edu.jxnu.base.entity.Memorandum;
//import cn.edu.jxnu.base.service.IMemorandumService;
//import cn.edu.jxnu.base.service.specification.SimpleSpecificationBuilder;
//import cn.edu.jxnu.base.service.specification.SpecificationOperator.Operator;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * 
// * 系统操作信息备忘录 控制类
// * 
// * @author 梦境迷离.
// * @time 2018年5月1日
// * @version v1.0
// */
//
//@Slf4j
//@Controller
//@RequestMapping("/admin/memorandum")
//public class MemorandumController extends BaseController {
//	@Autowired
//	private IMemorandumService memorandumService;
//
//	/**
//	 * 打开备忘录首页
//	 * 
//	 * @time 11点05分
//	 * @version V1.0
//	 * @return String
//	 */
//	@RequestMapping("/index")
//	public String index() {
//		return "admin/memorandum/index";
//	}
//
//	/**
//	 * 分页
//	 * 
//	 * @time 11点04分
//	 * @version V1.0
//	 * @return Page 类型 Memorandum
//	 */
//	@RequestMapping("/list")
//	@ResponseBody
//	public Page<Memorandum> list() {
//		SimpleSpecificationBuilder<Memorandum> builder = new SimpleSpecificationBuilder<Memorandum>();
//		/** 查询. */
//		String searchText = request.getParameter("searchText");
//		if (StringUtils.isNotBlank(searchText)) {
//			builder.add("userName", Operator.likeAll.name(), searchText);
//		}
//		Page<Memorandum> page = memorandumService.findAll(builder.generateSpecification(), getPageRequest());
//		log.info("Memorandum : {}", page.getSize());
//		return page;
//	}
//
//	/**
//	 * 删除
//	 * 
//	 * @time 11点06分
//	 * @version V1.0
//	 * @param id
//	 * @param map
//	 * @return JsonResult
//	 */
//	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
//	@ResponseBody
//	public JsonResult delete(@PathVariable Integer id, ModelMap map) {
//		try {
//			memorandumService.delete(id);
//			log.info("删除：{}成功",id);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return JsonResult.failure(e.getMessage());
//		}
//		return JsonResult.success();
//	}
//}
