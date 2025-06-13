package study.online.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import study.online.system.mapper.DictionaryMapper;
import study.online.system.model.po.Dictionary;
import study.online.system.service.IDictionaryService;

import java.util.List;

/**
 * 数据字典 服务实现类
 *
 * @author newmaster
 */
@Slf4j
@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements IDictionaryService {

	@Override
	public List<Dictionary> queryAll() {
		return this.list();
	}

	@Override
	public Dictionary getByCode(String code) {
		LambdaQueryWrapper<Dictionary> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Dictionary::getCode, code);

		return this.getOne(queryWrapper);
	}
}
