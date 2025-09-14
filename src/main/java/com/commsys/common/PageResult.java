package com.commsys.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果封装类
 * 
 * @author Xiaosu
 * @version 1.0.0
 * @since 2025-09-13
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据列表
     */
    private List<T> records;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页码
     */
    private Integer page;

    /**
     * 每页大小
     */
    private Integer size;

    /**
     * 总页数
     */
    private Integer pages;

    /**
     * 是否有下一页
     */
    private Boolean hasNext;

    /**
     * 是否有上一页
     */
    private Boolean hasPrevious;

    /**
     * 是否第一页
     */
    private Boolean isFirst;

    /**
     * 是否最后一页
     */
    private Boolean isLast;

    public PageResult() {
    }

    public PageResult(List<T> records, Long total, Integer page, Integer size) {
        this.records = records;
        this.total = total;
        this.page = page;
        this.size = size;
        this.pages = (int) Math.ceil((double) total / size);
        this.hasNext = page < pages;
        this.hasPrevious = page > 1;
        this.isFirst = page == 1;
        this.isLast = page.equals(pages);
    }

    /**
     * 从Spring Data Page对象创建分页结果
     * 
     * @param page Spring Data Page对象
     * @param <T> 数据类型
     * @return 分页结果
     */
    public static <T> PageResult<T> of(Page<T> page) {
        return new PageResult<>(
                page.getContent(),
                page.getTotalElements(),
                page.getNumber() + 1, // Spring Data页码从0开始，转换为从1开始
                page.getSize()
        );
    }

    /**
     * 创建空的分页结果
     * 
     * @param page 页码
     * @param size 每页大小
     * @param <T> 数据类型
     * @return 空的分页结果
     */
    public static <T> PageResult<T> empty(Integer page, Integer size) {
        return new PageResult<>(List.of(), 0L, page, size);
    }
}