package com.study.online.base.result;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 后端统一返回错误结果
 */
@Data
@AllArgsConstructor
public class ErrorResponse implements Serializable {

	/*错误信息*/
    private String msg;

}
