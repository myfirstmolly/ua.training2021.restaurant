package util;

import java.util.List;

public class Page<T> {

    private final int pageIndex;
    private final int totalPages;
    private final List<T> content;

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
