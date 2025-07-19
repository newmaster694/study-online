package study.online.base.constant;

public class ErrorMessageConstant {
	public static final String UNKNOW_ERROR = "执行过程异常，请重试！";
	public static final String PARAMS_ERROR = "非法参数";
	public static final String OBJECT_NULL = "空对象异常";
	public static final String QUERY_NULL = "查询结果为空";
	public static final String REQUEST_NULL = "必要请求参数为空";

	public static final String UN_FIND_COURSE = "课程不存在";
	public static final String UN_FIND_TEACH_PLAN = "教学计划不存在";

	public static final String COMPANY_INFORMATION_MISMATCH = "机构信息不匹配";
	public static final String FAIL_CREATE_COURSE_MARKET = "课程营销信息添加失败";
	public static final String FAIL_CREATE_COURSE_INFO = "课程基本信息添加失败";
	public static final String AUDIT_STATUS_MISMATCH = "审核状态不符";
	public static final String FAIL_ADD_TEACHER_INFO = "新增教师失败";
	public static final String FAIL_UPDATE_TEACHER_INFO = "更新教师失败";
	public static final String CHARGING_RULES_NOT_SELECTED = "收费规则未选择";
	public static final String ILLEGAL_CHARGES = "收费课程的收费金额必须要大于0";
	public static final String TEACH_PLAN_GRADE_ERROR = "只允许二级教学计划绑定媒资文件";

	public static final String SAVE_FILE_ERROR = "保存文件信息失败";
	public static final String FILE_EXIST_ERROR = "文件不存在";

	public static final String OVER_MAX_FAIL_COUNT_ERROR = "查出最大重试次数，请手工进行视频转码";
}