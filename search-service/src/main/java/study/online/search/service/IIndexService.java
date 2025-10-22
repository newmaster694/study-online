package study.online.search.service;

/**
 * 课程索引service
 *
 * @author Mr.M
 * @version 1.0
 * @since 2022/9/24 22:40
 */
public interface IIndexService {

	/**
	 * 添加索引
	 *
	 * @param indexName 索引名称
	 * @param id        主键
	 * @param object    索引对象
	 * @return Boolean true表示成功,false失败
	 * @author Mr.M
	 * @since 2022/9/24 22:57
	 */
	public Boolean addCourseIndex(String indexName, String id, Object object);


	/**
	 * 更新索引
	 *
	 * @param indexName 索引名称
	 * @param id        主键
	 * @param object    索引对象
	 * @return Boolean true表示成功,false失败
	 * @author Mr.M
	 * @since 2022/9/25 7:49
	 */
	public Boolean updateCourseIndex(String indexName, String id, Object object);

	/**
	 * 删除索引
	 *
	 * @param indexName 索引名称
	 * @param id        主键
	 * @return java.lang.Boolean
	 * @author Mr.M
	 * @since 2022/9/25 9:27
	 */
	public Boolean deleteCourseIndex(String indexName, String id);

}
