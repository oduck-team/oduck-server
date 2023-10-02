package io.oduck.api.global.common;

import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
public class SliceResponse<T extends EntityBased> {
    private final List<T> items;
    private final int size;             // 한 페이지에 보여줄 아이템의 개수
    private final boolean isLastPage;   // 마지막 페이지일 경우, true 반환.
    private Long lastId;                // 마지막 아이템의 id
    public SliceResponse(Slice<T> sliceContent) {
        this.items = sliceContent.getContent();
        this.size = sliceContent.getSize();
        this.isLastPage = sliceContent.isLast();

        if (!items.isEmpty() && !isLastPage) {
            T lastItem = items.get(items.size() - 1);
            lastId = lastItem.getId();
        }
    }

    public static <T extends EntityBased> SliceResponse<T> of(Slice<T> sliceContent) {
        return new SliceResponse<>(sliceContent);
    }
}