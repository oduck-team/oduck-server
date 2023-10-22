package io.oduck.api.domain.bookmark.controller;

import io.oduck.api.domain.bookmark.dto.BookmarkReqDto.CreateReq;
import io.oduck.api.domain.bookmark.service.BookmarkService;
import io.oduck.api.global.security.auth.dto.AuthUser;
import io.oduck.api.global.security.auth.dto.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<?> postBookmark(
        @LoginUser AuthUser user,
        @RequestBody @Valid CreateReq body) {
        boolean isCreated = bookmarkService.toggleBookmark(user.getId(), body.getAnimeId());
        return ResponseEntity
            .status(isCreated ? HttpStatus.CREATED : HttpStatus.NO_CONTENT)
            .build();
    }

    @GetMapping("/{animeId}")
    public ResponseEntity<?> getBookmark(
        @PathVariable("animeId") Long animeId,
        @LoginUser AuthUser user) {
        return ResponseEntity.ok(bookmarkService.checkBookmarked(user.getId(), animeId));
    }
}