package io.oduck.api.global.stub;

import io.oduck.api.domain.bookmark.entity.Bookmark;
import java.util.ArrayList;
import java.util.List;

public class BookmarkStub {
    List<Bookmark> bookmarks = new ArrayList<>();

    public BookmarkStub() {
        Bookmark bookmark1 = Bookmark.builder()
            .build();

        Bookmark bookmark2 = Bookmark.builder()
            .build();

        Bookmark bookmark3 = Bookmark.builder()
            .build();

        bookmarks.addAll(List.of(bookmark1, bookmark2, bookmark3));
    }

    public List<Bookmark> getBookmarks() {
        return bookmarks;
    }
}
