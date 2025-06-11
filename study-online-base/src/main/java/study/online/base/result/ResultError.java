package study.online.base.result;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 后端统一返回错误结果
 */
@Data
@AllArgsConstructor
public class ResultError implements Serializable {

	/*错误信息*/
    private List<String> msgs;

}
