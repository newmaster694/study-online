package study.online.messagesdk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import study.online.messagesdk.mapper.MqMessageHistoryMapper;
import study.online.messagesdk.model.po.MqMessageHistory;
import study.online.messagesdk.service.MqMessageHistoryService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class MqMessageHistoryServiceImpl
	extends ServiceImpl<MqMessageHistoryMapper, MqMessageHistory>
	implements MqMessageHistoryService {
}
