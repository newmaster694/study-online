package study.online.content.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.online.base.exception.BaseException;
import study.online.content.mapper.TeachplanMapper;
import study.online.content.mapper.TeachplanMediaMapper;
import study.online.content.model.dto.BindTeachPlanMediaDTO;
import study.online.content.model.dto.SaveTeachplanDTO;
import study.online.content.model.dto.TeachplanDTO;
import study.online.content.model.po.Teachplan;
import study.online.content.model.po.TeachplanMedia;
import study.online.content.service.ITeachplanService;

import java.time.LocalDateTime;
import java.util.List;

import static study.online.base.constant.ErrorMessageConstant.TEACH_PLAN_GRADE_ERROR;
import static study.online.base.constant.ErrorMessageConstant.UN_FIND_TEACH_PLAN;

@Service
public class TeachplanServiceImpl extends ServiceImpl<TeachplanMapper, Teachplan> implements ITeachplanService {

	@Resource
	private TeachplanMapper teachplanMapper;

	@Resource
	private TeachplanMediaMapper teachplanMediaMapper;

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

	@Override
	@Transactional
	public void deleteTeachplan(Long teachplanId) {
		Long parentid = this.getById(teachplanId).getParentid();
		if (parentid == 0) {//大章节，必须保证下面没有小章节了才能删
			//查找小章节
			LambdaQueryWrapper<Teachplan> teachplanQueryWrapper = new LambdaQueryWrapper<>();
			teachplanQueryWrapper.eq(Teachplan::getParentid, teachplanId);//这里比较的是找有没有其他记录的parentid等于这个大章节的id
			long count = this.count(teachplanQueryWrapper);
			if (count > 0) {
				throw new BaseException("课程计划还有子级信息，无法操作");
			} else {
				this.removeById(teachplanId);
				LambdaQueryWrapper<TeachplanMedia> teachplanMediaQueryWrapper = new LambdaQueryWrapper<>();
				teachplanMediaQueryWrapper.eq(TeachplanMedia::getTeachplanId, teachplanId);
				teachplanMediaMapper.delete(teachplanMediaQueryWrapper);
			}
		} else { //小章节，直接删
			this.removeById(teachplanId);
			LambdaQueryWrapper<TeachplanMedia> teachplanMediaQueryWrapper = new LambdaQueryWrapper<>();
			teachplanMediaQueryWrapper.eq(TeachplanMedia::getTeachplanId, teachplanId);
			teachplanMediaMapper.delete(teachplanMediaQueryWrapper);
		}
	}

	@Override
	@Transactional
	public void move(String moveType, Long teachplanId) {
		Teachplan teachplan = this.getById(teachplanId);
		if (teachplan == null) {
			throw new BaseException("课程计划不存在");
		}

		switch (moveType) {
			case "moveup":
				this.moveNode(teachplan, -1);
				break;
			case "movedown":
				this.moveNode(teachplan, 1);
				break;
			default:
				throw new BaseException("操作异常，请联系管理员！");
		}
	}

	@Override
	@Transactional
	public TeachplanMedia associationMedia(BindTeachPlanMediaDTO bindTeachPlanMediaDTO) {
		Long teachPlanId = bindTeachPlanMediaDTO.getTeachPlanId();

		Teachplan teachplan = this.getById(teachPlanId);
		if (teachplan == null) {
			BaseException.cast(UN_FIND_TEACH_PLAN);
		}

		if (teachplan.getGrade() != 2) {
			BaseException.cast(TEACH_PLAN_GRADE_ERROR);
		}

		//先删除原来教学计划绑定的媒资
		teachplanMediaMapper.delete(new LambdaQueryWrapper<TeachplanMedia>()
			.eq(TeachplanMedia::getTeachplanId, teachPlanId));

		//再添加教学计划与绑定的媒资关系
		TeachplanMedia teachplanMedia = TeachplanMedia.builder()
			.courseId(teachplan.getCourseId())
			.teachplanId(teachPlanId)
			.mediaFilename(bindTeachPlanMediaDTO.getFileName())
			.mediaId(bindTeachPlanMediaDTO.getMediaId())
			.createDate(LocalDateTime.now())
			.build();

		teachplanMediaMapper.insert(teachplanMedia);

		return teachplanMedia;
	}

	@Override
	public void unbindMedia(Long teachplanId, String mediaId) {
		Teachplan teachplan = this.getById(teachplanId);
		if (teachplan == null) {
			BaseException.cast(UN_FIND_TEACH_PLAN);
		}

		teachplanMediaMapper.delete(new LambdaQueryWrapper<TeachplanMedia>()
			.eq(TeachplanMedia::getTeachplanId, teachplan)
			.eq(TeachplanMedia::getMediaId,mediaId));
	}

	/**
	 * 移动章节节点
	 *
	 * @param teachplan 要移动的章节
	 * @param index     移动的节点索引
	 */
	private void moveNode(Teachplan teachplan, int index) {
		Teachplan preNode = lambdaQuery()
			.eq(Teachplan::getParentid, teachplan.getParentid())
			.eq(Teachplan::getCourseId, teachplan.getCourseId())
			.eq(Teachplan::getOrderby, teachplan.getOrderby() + index)
			.one();

		if (preNode == null) {
			if (index == 1) {
				throw new BaseException("当前节点已是最后！");
			} else if (index == -1) {
				throw new BaseException("当前节点已是最前！");
			} else {
				throw new BaseException("操作异常，请联系管理员！");
			}
		}

		preNode.setOrderby(teachplan.getOrderby());
		teachplan.setOrderby(teachplan.getOrderby() + index);
		this.updateById(teachplan);
		this.updateById(preNode);
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
