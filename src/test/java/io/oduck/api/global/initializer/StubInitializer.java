//package io.oduck.api.global.initializer;
//
//import io.oduck.api.domain.anime.entity.Anime;
//import io.oduck.api.domain.anime.repository.AnimeRepository;
//import io.oduck.api.domain.bookmark.entity.Bookmark;
//import io.oduck.api.domain.bookmark.repository.BookmarkRepository;
//import io.oduck.api.domain.member.entity.Member;
//import io.oduck.api.domain.member.repository.MemberRepository;
//import io.oduck.api.domain.starRating.entity.StarRating;
//import io.oduck.api.domain.starRating.repository.StarRatingRepository;
//import io.oduck.api.global.stub.BookmarkStub;
//import io.oduck.api.global.stub.StarRatingStub;
//import java.util.List;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//import org.springframework.test.context.ActiveProfiles;
//
//@Component
//@ActiveProfiles("test")
//public class StubInitializer implements ApplicationRunner {
//    @Autowired
//    MemberRepository memberRepository;
//
//    @Autowired
//    AnimeRepository animeRepository;
//
//    @Autowired
//    BookmarkRepository bookmarkRepository;
//
//    @Autowired
//    StarRatingRepository starRatingRepository;
//
//    int bookmarkIterator = 0;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        Member member = memberRepository.findById(1L).get();
//        List<Member> members = memberRepository.findAll();
//        List<Anime> animes = animeRepository.findAll();
//
//        BookmarkStub bookmarkStub = new BookmarkStub();
//
//        bookmarkStub.getBookmarks().forEach(bookmark -> {
//            bookmark.relateAnime(animes.get(bookmarkIterator));
//            bookmark.relateMemer(member);
//            bookmarkIterator++;
//        });
//
//        bookmarkRepository.saveAll(bookmarkStub.getBookmarks());
//
//        StarRatingStub starRatingStub = new StarRatingStub();
//
//        List<StarRating> starRatings = starRatingStub.getStarRatings();
//
//        StarRating starRating1 = starRatings.get(0);
//        starRating1.relateAnime(animes.get(0));
//        starRating1.relateMember(members.get(0));
//
//        StarRating starRating2 = starRatings.get(1);
//        starRating2.relateAnime(animes.get(1));
//        starRating2.relateMember(members.get(0));
//
//        StarRating starRating3 = starRatings.get(2);
//        starRating3.relateAnime(animes.get(0));
//        starRating3.relateMember(members.get(1));
//
//        StarRating starRating4 = starRatings.get(3);
//        starRating4.relateAnime(animes.get(0));
//        starRating4.relateMember(members.get(2));
//
//        starRatingRepository.saveAll(starRatings);
//        List<Bookmark> bookmarkList = bookmarkRepository.findAll();
//        List<StarRating> starRatingList = starRatingRepository.findAll();
//    }
//}
