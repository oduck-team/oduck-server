package io.oduck.api.global.common;

import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
public class SliceResponse<T extends EntityBased> {
    private final List<T> items;
    private final int size;          // 한 페이지에 보여줄 아이템의 개수
    private final boolean hasNext;   // 마지막 페이지일 경우, true 반환.
    private String lastId;           // 마지막 아이템의 id

    public SliceResponse(Slice<T> sliceContent, String property) {
        this.items = sliceContent.getContent();
        this.size = sliceContent.getSize();
        this.hasNext = sliceContent.isLast();

        if (!items.isEmpty() && !hasNext) {
            T lastItem = items.get(items.size() - 1);
            lastId = lastItem.bringId(property);
        }
    }

    public SliceResponse(Slice<?> slice, List<T> items, String property) {
        this.items = items;
        this.size = slice.getSize();
        this.hasNext = slice.hasNext();

        if (!items.isEmpty() && hasNext) {
            T lastItem = items.get(items.size() - 1);
            this.lastId = lastItem.bringId(property);
        } else {
            this.lastId = "";
        }
    }

    public static <T extends EntityBased> SliceResponse<T> of(Slice<T> slice, String property) {
        return new SliceResponse<>(slice, property);
    }

    public static <T extends EntityBased> SliceResponse<T> of(Slice<?> slice, List<T> items, String property) {
        return new SliceResponse<>(slice, items, property);
    }
}