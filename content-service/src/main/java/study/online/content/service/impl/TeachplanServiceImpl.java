package study.online.content.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import study.online.content.mapper.TeachplanMapper;
import study.online.content.model.dto.SaveTeachplanDTO;
import study.online.content.model.dto.TeachplanDTO;
import study.online.content.model.po.Teachplan;
import study.online.content.service.ITeachplanService;

import java.util.List;

@Service
public class TeachplanServiceImpl extends ServiceImpl<TeachplanMapper, Teachplan> implements ITeachplanService {

	@Resource
	private TeachplanMapper teachplanMapper;

	@Override
	public List<TeachplanDTO> findTeachplanTree(long courseId) {
		return teachplanMapper.selectTreeNodes(courseId);
	}

	@Override
	public void saveTeachplan(SaveTeachplanDTO saveTeachplanDTO) {
		Long id = saveTeachplanDTO.getId();
		if (id != null) {
			Teachplan teachplan = this.getById(id);
			BeanUtil.copyProperties(saveTeachplanDTO, teachplan, true);
			this.updateById(teachplan);
		} else {
			int count = this.getTeachplanCount(saveTeachplanDTO.getCourseId(), saveTeachplanDTO.getParentid());
			Teachplan teachplan = new Teachplan();
			teachplan.setOrderby(count + 1);
			BeanUtil.copyProperties(saveTeachplanDTO, teachplan, true);

			this.save(teachplan);
		}
	}

	/**
	 * 获取最新的排序号
	 *
	 * @param courseId 课程id
	 * @param parentId 父课程的计划id
	 * @return 最新的排序号
	 */
	private int getTeachplanCount(Long courseId, Long parentId) {
		LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();

		queryWrapper.eq(Teachplan::getCourseId, courseId);
		queryWrapper.eq(Teachplan::getParentid, parentId);

		return (int) this.count(queryWrapper);
	}
}
