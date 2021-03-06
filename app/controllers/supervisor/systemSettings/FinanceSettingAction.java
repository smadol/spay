package controllers.supervisor.systemSettings;

import business.GateWay;
import business.Member;
import business.Platform;
import constants.Constants;
import controllers.supervisor.SupervisorController;
import models.t_payment_gateways;
import models.v_member_details;
import models.v_member_events;
import models.v_platforms;
import org.apache.commons.lang.StringUtils;
import utils.ErrorInfo;
import utils.PageBean;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 财务设置
 * 
 * @author bsr
 * 
 */
public class FinanceSettingAction extends SupervisorController {
	
	/**
	 * 资金托管账户设置
	 */
	public static void managedFunds(long gatewayId) {
		ErrorInfo error = new ErrorInfo();
		if(gatewayId <= 0) {
			gatewayId = 1;
		}
		
		GateWay gateway = new GateWay();
		gateway.id = gatewayId;
		
		List<t_payment_gateways> ways = GateWay.queryAll(error);
		
		if(error.code < 0) {
			render(Constants.ERROR_PAGE_PATH_SUPERVISOR);
		}
		
		render(gateway, ways);
	}
	
	/**
	 * 保存资金托管账户设置
	 */
	public static void saveManagedFunds(long gatewayId, boolean isUse, String name, String pid, String account, String key, String CERT_MD5, String PUB_KEY,
			String DES_KEY, String DES_IV, String test1, String test2, String test3) {
		ErrorInfo error = new ErrorInfo();
		
		if(gatewayId <= 0) {
			flash.error("传入参数有误");
			
			managedFunds(gatewayId);
		}
		
		if(StringUtils.isBlank(name) || StringUtils.isBlank(pid) || StringUtils.isBlank(account)) {
			flash.error("平台名称，商户ID或账户不能为空");
			
			managedFunds(gatewayId);
		}
		
		GateWay gateway = new GateWay();
		Map<String, String> keyInfo = new HashMap<String, String>();

		gateway.name = name;
		gateway.account = account;
		gateway.pid = pid;
		gateway.key = key;
		gateway.keyInfo = keyInfo;
		gateway.isUse = isUse;

		gateway.update(gatewayId, error);

		flash.error(error.msg);

		managedFunds(gatewayId);
	}
	
	/**
	 * 资金托管接入平台设置
	 */
	public static void joinSetting(long platformId) {
		ErrorInfo error = new ErrorInfo();
		List<t_payment_gateways> ways = GateWay.queryAll(error);
		
		Platform platform = null;
		
		if(platformId > 0) {
			platform = new Platform();
			platform.id = platformId;
			
			if(platform.dealStatus) {
				platform = null;
			}
		}
		
		
		
		if(error.code < 0) {
			render(Constants.ERROR_PAGE_PATH_SUPERVISOR);
		}
		render(platform, ways);
	}
	
	/**
	 * 保存资金托管接入平台设置
	 */
	public static void saveJoinSetting(int gateway, String name, String domain, String encryption) {
		ErrorInfo error = new ErrorInfo();
		
		flash.put("gateway", gateway);
		flash.put("name", name);
		flash.put("domain", domain);
		flash.put("encryption", encryption);
		
		if(gateway <= 0) {
			flash.error("请选择正确的支付账户");
			
			joinSetting(0);
		}
		
		if(StringUtils.isBlank(name)) {
			flash.error("请输入公司名称");
			
			joinSetting(0);
		}
		
		if(StringUtils.isBlank(domain)) {
			flash.error("请输入绑定域名");
			
			joinSetting(0);
		}
		
		if(StringUtils.isBlank(encryption)) {
			flash.error("请输入约定密钥");
			
			joinSetting(0);
		}
		
		Platform platform =  new Platform();
		
		platform.gatewayId = gateway;
		platform.name = name;
		platform.domain = domain;
		platform.encryption = encryption;
		
		platform.savePlatform(error);
		
		flash.error(error.msg);
		joinSetting(0);
	}
	
	/**
	 * 平台交易记录
	 */
	public static void dealDetails(long memberId, int condition, String keyword, Date startDate, Date endDate, int orderStatus, int currPage, int pageSize) {
		ErrorInfo error = new ErrorInfo();
		PageBean<v_member_details> pageBean = Member.queryMemberDetails(memberId, condition, keyword, startDate, endDate, orderStatus, currPage, pageSize, error);
		
		if(error.code < 0) {
			render(Constants.ERROR_PAGE_PATH_SUPERVISOR);
		}
		
		render(pageBean);
	}
	
	/**
	 * 用户操作记录
	 */
	public static void eventDetails(int condition, String keyword, Date startDate, Date endDate, int orderStatus, int currPage, int pageSize) {
		ErrorInfo error = new ErrorInfo();
		PageBean<v_member_events> pageBean = Member.queryMemberEvents(condition, keyword, startDate, endDate, orderStatus, currPage, pageSize, error);
		
		if(error.code < 0) {
			render(Constants.ERROR_PAGE_PATH_SUPERVISOR);
		}
		
		render(pageBean);
	}
	
	/**
	 * 平台接入详情
	 */
	public static void bindPlatform(int condition, String keyword, Date startDate, Date endDate, int orderStatus, int currPage, int pageSize) {
		ErrorInfo error = new ErrorInfo();
		PageBean<v_platforms> pageBean = Platform.queryPlatform(condition, keyword, startDate, endDate, orderStatus, currPage, pageSize, error);
		
		if(error.code < 0) {
			render(Constants.ERROR_PAGE_PATH_SUPERVISOR);
		}
		
		render(pageBean);
	}
	
	public static void updatePlatform(long platformId, int gateway, String name, String domain, String encryption) {
		ErrorInfo error = new ErrorInfo();
		
//		flash.put("gateway", gateway);
//		flash.put("name", name);
//		flash.put("domain", domain);
//		flash.put("encryption", encryption);
		
		if(platformId <= 0) {
			flash.error("请求参数有误");
			
			joinSetting(platformId);
		}
		
		if(gateway <= 0) {
			flash.error("请选择正确的支付账户");
			
			joinSetting(platformId);
		}
		
		if(StringUtils.isBlank(name)) {
			flash.error("请输入公司名称");
			
			joinSetting(platformId);
		}
		
		if(StringUtils.isBlank(domain)) {
			flash.error("请输入绑定域名");
			
			joinSetting(platformId);
		}
		
		if(StringUtils.isBlank(encryption)) {
			flash.error("请输入约定密钥");
			
			joinSetting(platformId);
		}
		
		Platform platform =  new Platform();
		
		platform.gatewayId = gateway;
		platform.name = name;
		platform.domain = domain;
		platform.encryption = encryption;
		
		platform.updatePlatform(platformId, error);
		
		flash.error(error.msg);
		
		bindPlatform(0,null, null, null, 0, 1, 10);
	}
}
