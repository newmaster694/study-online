package study.online.base.constant;

public interface ErrorMessage {
	String UNKNOW_ERROR = "执行过程异常，请重试！";
	String OBJECT_NULL = "空对象异常";
	String QUERY_NULL = "查询结果为空";
	String REQUEST_NULL = "必要请求参数为空";

	String COMPANY_INFORMATION_MISMATCH = "机构信息不匹配";
	String FAIL_CREATE_COURSE_MARKET = "课程营销信息添加失败";
	String FAIL_CREATE_COURSE_INFO = "课程基本信息添加失败";

	String AUDIT_STATUS_MISMATCH = "审核状态不符";
	String AUDIT_STATUS_ERROR = "课程已提交，请等待审核";

	String COURSE_PIC_NULL_ERROR = "请上传课程图片";

	String FAIL_ADD_TEACHER_INFO = "新增教师失败";
	String FAIL_UPDATE_TEACHER_INFO = "更新教师失败";

	String CHARGING_RULES_NOT_SELECTED = "收费规则未选择";
	String ILLEGAL_CHARGES = "收费课程的收费金额必须要大于0";

	String TEACH_PLAN_GRADE_ERROR = "只允许二级教学计划绑定媒资文件";
	String TEACH_PLAN_NULL_ERROR = "请编写课程计划";

	String SAVE_FILE_ERROR = "保存文件信息失败";
	String FILE_EXIST_ERROR = "文件不存在";

	String OVER_MAX_FAIL_COUNT_ERROR = "查出最大重试次数，请手工进行视频转码";

	String PREVIEW_MODEL_ERROR = "预览模型未找到";

	String COURSE_STATIC_ERROR = "课程静态化异常";

	String VALDATE_PARAMETER_ERROR = "参数校验失败";
	String REGISTER_USER_ERROR = "用户未注册";
	String AUTH_ERROR = "登录失败";

	String UNKNOW_LOGIN_OAUTH_PLATEFORM = "无对应的第三方登录平台";
	String UNKNOW_JUSTAUTH_CONFIG = "未配置第三方平台数据";
}