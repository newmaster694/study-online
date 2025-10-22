package study.online.search.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import study.online.base.model.PageResult;

import java.util.List;

/**
 * @author Mr.M
 * @version 1.0
 * @since 2022/9/25 17:51
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class SearchPageResultDto<T> extends PageResult<T> {

	//大分类列表
	List<String> mtList;
	//小分类列表
	List<String> stList;

	public SearchPageResultDto(List<T> items, long counts, long page, long pageSize) {
		super(items, counts, page, pageSize);
	}

}
