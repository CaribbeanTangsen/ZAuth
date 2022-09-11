package cn.lijilong.zauth.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

@Data
@ApiModel("返回值")
public class RequestDataDTO<T> {


    @ApiModelProperty("状态码")
    private int code;
    @ApiModelProperty("提示信息")
    private String msg;
    @ApiModelProperty("数据")
    private T data;


    public static RequestDataDTO<Object> buildError(int code, String msg) {
        RequestDataDTO<Object> requestDataDTO = new RequestDataDTO<>();
        requestDataDTO.setCode(code);
        requestDataDTO.setMsg(msg);
        return requestDataDTO;
    }
    public static RequestDataDTO<Object> buildError(int code) {
        RequestDataDTO<Object> requestDataDTO = new RequestDataDTO<>();
        requestDataDTO.setCode(code);
        requestDataDTO.setMsg("操作失败！");
        return requestDataDTO;
    }

    public static <T> RequestDataDTO<T> buildSuccess(String msg, T data) {
        RequestDataDTO<T> requestDataDTO = new RequestDataDTO<T>();
        requestDataDTO.setCode(200);
        requestDataDTO.setMsg(msg);
        requestDataDTO.setData(data);
        return requestDataDTO;
    }

    public static <T> RequestDataDTO<T> buildSuccess(T data) {
        RequestDataDTO<T> requestDataDTO = new RequestDataDTO<>();
        requestDataDTO.setCode(200);
        requestDataDTO.setData(data);
        return requestDataDTO;
    }

    public static RequestDataDTO<Object> buildSuccess() {
        RequestDataDTO<Object> requestDataDTO = new RequestDataDTO<>();
        requestDataDTO.setCode(200);
        return requestDataDTO;
    }

    public static RequestDataDTO<Object> buildSuccess(String msg) {
        RequestDataDTO<Object> requestDataDTO = new RequestDataDTO<>();
        requestDataDTO.setCode(200);
        requestDataDTO.setMsg(msg);
        return requestDataDTO;
    }

    public static RequestDataDTO<Object> buildAddSuccess(Map<String, Object> map) {
        RequestDataDTO<Object> requestDataDTO = new RequestDataDTO<>();
        requestDataDTO.setCode((int)map.get("code"));
        requestDataDTO.setMsg((String)map.get("msg"));
        return requestDataDTO;
    }
}
