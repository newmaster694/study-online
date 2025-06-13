package study.online.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import study.online.system.model.po.Dictionary;

import java.util.List;

/**
 * 数据字典 服务类
 *
 * @author newmaster
 * @since 2021-10-07
 */
public interface IDictionaryService extends IService<Dictionary> {

	/**
	 * 查询所有数据字典内容
	 *
	 * @return List<Dictionary>
	 */
	List<Dictionary> queryAll();

	/**
	 * 根据code查询数据字典
	 *
	 * @param code -- String 数据字典Code
	 * @return Dictionary
	 */
	Dictionary getByCode(String code);
}
