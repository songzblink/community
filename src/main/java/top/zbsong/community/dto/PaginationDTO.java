package top.zbsong.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO<T> {
    private List<T> data;
    private Boolean showFirstPage;
    private Boolean showEndPage;
    private Boolean showPrevious;
    private Boolean showNext;
    private Integer totalPage;
    private Integer currentPage;
    private List<Integer> pages = new ArrayList<>();

    public void setPagination(Integer totalCount, Integer page, Integer size) {
        currentPage = page;

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }


        for (int i = -3; i <= 3; i++) {
            int temp = page + i;
            if (temp > 0 && temp <= totalPage) {
                pages.add(temp);
            }
        }

        // 是否展示上一页
        if (page != 1) {
            showPrevious = true;
        }
        // 是否展示下一页
        if (!page.equals(totalPage)) {
            showNext = true;
        }
        // 是否展示第一页
        if (page > 4) {
            showFirstPage = true;
        }
        // 是否展示最后一页
        if (totalPage - page + 1 > 4) {
            showEndPage = true;
        }

    }
}
