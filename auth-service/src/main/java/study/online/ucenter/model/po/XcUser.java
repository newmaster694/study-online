package study.online.ucenter.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author itcast
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("xc_user")
@Accessors(chain = true)
public class XcUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String password;

    private String salt;

    private String name;
    private String nickname;
    private String wxUnionid;
    private String companyId;

    /**
     * 头像
     */
    private String userpic;

    private String utype;

    private LocalDateTime birthday;

    private String sex;

    private String email;

    private String cellphone;

    private String qq;

    /**
     * 用户状态
     */
    private String status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
