package study.online.ucenter.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import study.online.ucenter.model.po.XcUser;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户扩展信息
 *
 * @author Mr.M
 * @since 2022/9/30 13:56
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class XcUserExt extends XcUser {
    //用户权限
    List<String> permissions = new ArrayList<>();
}
