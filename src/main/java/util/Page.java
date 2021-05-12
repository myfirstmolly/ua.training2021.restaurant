package util;

import java.util.List;

public class Page<T> {

    int pageIndex;
    int totalPages;
    List<T> content;

    public Page(int pageIndex, int totalPages, List<T> content) {
        this.pageIndex = pageIndex;
        this.totalPages = totalPages;
        this.content = content;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<T> getContent() {
        return content;
    }

}
