package study.online.ucenter.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证用户请求参数
 *
 * @author Mr.M
 * @version 1.0
 * @since 2022/9/29 10:56
 */
@Data
@Builder
public class AuthParamsDTO {

	private String id;//用户id
    private String username; //用户名
    private String password; //域  用于扩展
    private String cellphone;//手机号
    private String checkcode;//验证码
    private String checkcodekey;//验证码key
    private Map<String, Object> payload;//附加数据，作为扩展，不同认证类型可拥有不同的附加数据。如认证类型为短信时包含smsKey : sms:3d21042d054548b08477142bbca95cfa; 所有情况下都包含clientId
}
