package cn.lijilong.zauth.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@ApiModel("分页数据返回值")
public class PageSimple<T> {

    @ApiModelProperty("当前页")
    private int pageNumber;
    @ApiModelProperty("每页数据量")
    private int pageSize;
    @ApiModelProperty("元素总量")
    private int totalPages;
    @ApiModelProperty("总页数")
    private Long totalElements;
    @ApiModelProperty("数据")
    private List<T> contents;

    public static <T> PageSimple<T> build(Page<T> page) {
        PageSimple<T> pageSimple = new PageSimple<>();
        pageSimple.setPageNumber(page.getPageable().getPageNumber() + 1);
        pageSimple.setPageSize(page.getPageable().getPageSize());
        pageSimple.setTotalElements(page.getTotalElements());
        pageSimple.setTotalPages(page.getTotalPages());
        pageSimple.setContents(page.getContent());
        return pageSimple;
    }


}
