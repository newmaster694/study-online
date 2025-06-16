package study.online.base.model;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@ToString
@Accessors(chain = true)
public class RestResponse<T> {

	/*响应编码,0为正常,-1错误*/
	private Integer code;

	/*响应提示信息*/
	private String msg;

	/*错误提示信息集合*/
	private List<String> errormsg;

	/*响应内容*/
	private T result;

	public RestResponse() {
		this(0, "success");
	}

	public RestResponse(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	/*错误信息封装*/
	public static <T> RestResponse<T> validFail(String msg) {
		RestResponse<T> restResponse = new RestResponse<>();
		return restResponse.setCode(-1).setMsg(msg);
	}

	public static <T> RestResponse<T> validFail(List<String> errormsg) {
		RestResponse<T> restResponse = new RestResponse<>();
		return restResponse.setCode(-1).setErrormsg(errormsg);
	}

	public static <T> RestResponse<T> validFail(T result, String msg) {
		RestResponse<T> restResponse = new RestResponse<>();
		return restResponse.setCode(-1).setMsg(msg).setResult(result);
	}

	/*正常响应*/
	public static <T> RestResponse<T> success(T result) {
		RestResponse<T> restResponse = new RestResponse<>();
		return restResponse.setCode(0).setResult(result);
	}

	public static <T> RestResponse<T> success(T result,String msg) {
		RestResponse<T> restResponse = new RestResponse<>();
		return restResponse.setCode(0).setMsg(msg).setResult(result);
	}

	public static <T> RestResponse<T> success() {
		return new RestResponse<>();
	}

	public Boolean isSuccessful() {
		return this.code == 0;
	}
}
