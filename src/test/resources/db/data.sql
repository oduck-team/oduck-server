INSERT INTO member(created_at, updated_at, login_type, `role`) VALUES('2023-10-10 21:05:31.859', '2023-10-10 21:05:31.859', 'LOCAL', 'MEMBER');
INSERT INTO auth_local(created_at, member_id, updated_at, password, email) VALUES('2023-10-10 21:05:31.859', 1, '2023-10-10 21:05:31.859', '{bcrypt}$2a$10$C0G6uQz.MzfsSH7BZFRBz.MPBmFSV2zAloqqBIwaUpxnmMgCQK..i', 'oduckdmin@gmail.com');
INSERT INTO member_profile (created_at, member_id, updated_at, name, info, thumbnail) VALUES('2023-10-10 21:05:31.859', 1, '2023-10-10 21:05:31.859', 'admin', 'admin info', 'http://thumbnail.com');

INSERT INTO member(created_at, updated_at, login_type, `role`) VALUES('2023-10-11 21:05:31.859', '2023-10-11 21:05:31.859', 'LOCAL', 'MEMBER');
INSERT INTO auth_local(created_at, member_id, updated_at, password, email) VALUES('2023-10-11 21:05:31.859', 2, '2023-10-11 21:05:31.859', '{bcrypt}$2a$10$C0G6uQz.MzfsSH7BZFRBz.MPBmFSV2zAloqqBIwaUpxnmMgCQK..i', 'john@gmail.com');
INSERT INTO member_profile (created_at, member_id, updated_at, name, info, thumbnail) VALUES('2023-10-11 21:05:31.859', 2, '2023-10-11 21:05:31.859', 'john', 'john info', 'http://thumbnail.com');

INSERT INTO member(created_at, updated_at, login_type, `role`) VALUES('2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859', 'LOCAL', 'MEMBER');
INSERT INTO auth_local(created_at, member_id, updated_at, password, email) VALUES('2023-10-12 21:05:31.859', 3, '2023-10-12 21:05:31.859', '{bcrypt}$2a$10$C0G6uQz.MzfsSH7BZFRBz.MPBmFSV2zAloqqBIwaUpxnmMgCQK..i', 'david@gmail.com');
INSERT INTO member_profile (created_at, member_id, updated_at, name, info, thumbnail) VALUES('2023-10-12 21:05:31.859', 3, '2023-10-12 21:05:31.859', 'david', 'david info', 'http://thumbnail.com');

INSERT INTO member(created_at, updated_at, login_type, `role`) VALUES('2023-10-13 21:05:31.859', '2023-10-13 21:05:31.859', 'LOCAL', 'MEMBER');
INSERT INTO auth_local(created_at, member_id, updated_at, password, email) VALUES('2023-10-13 21:05:31.859', 4, '2023-10-13 21:05:31.859', '{bcrypt}$2a$10$C0G6uQz.MzfsSH7BZFRBz.MPBmFSV2zAloqqBIwaUpxnmMgCQK..i', 'reina@gmail.com');
INSERT INTO member_profile (created_at, member_id, updated_at, name, info, thumbnail) VALUES('2023-10-13 21:05:31.859', 4, '2023-10-13 21:05:31.859', 'reina', 'reina info', 'http://thumbnail.com');

INSERT INTO series(created_at, title, updated_at) VALUES('2023-10-10 21:05:31.859', '귀멸의 칼날', '2023-10-10 21:05:31.859');
INSERT INTO series(created_at, title, updated_at) VALUES('2023-10-10 21:05:31.859', '원피스', '2023-10-10 21:05:31.859');
INSERT INTO series(created_at, title, updated_at) VALUES('2023-10-10 21:05:31.859', '나루토', '2023-10-10 21:05:31.859');

INSERT INTO anime(episode_count, is_released, created_at, updated_at, title, thumbnail, broadcast_type, quarter, rating, status, summary, release_year, series_id)
VALUES(0, 1, '2023-10-10 21:05:31.859', '2023-10-10 21:05:31.859', '강연금', 'http://thumbnail.com', 'TVA', 'Q1', 'ALL', 'ONGOING', '1', 2009, 1);
INSERT INTO anime(episode_count, is_released, created_at, updated_at, title, thumbnail, broadcast_type, quarter, rating, status, summary, release_year, series_id)
VALUES(0, 1, '2023-10-11 21:05:31.859', '2023-10-11 21:05:31.859', '스파패', 'http://thumbnail.com', 'TVA', 'Q2', 'ALL', 'ONGOING', '2', 2023, 2);
INSERT INTO anime(episode_count, is_released, created_at, updated_at, title, thumbnail, broadcast_type, quarter, rating, status, summary, release_year, series_id)
VALUES(0, 1, '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859', '귀멸칼날', 'http://thumbnail.com', 'TVA', 'Q3', 'ALL', 'ONGOING', '3', 2022, 3);

INSERT INTO original_author(name, created_at, updated_at) VALUES('고토게 코요하루', '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');
INSERT INTO original_author(name, created_at, updated_at) VALUES('엔도 타츠야', '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');
INSERT INTO original_author(name, created_at, updated_at) VALUES('아라카와 히로무', '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');

INSERT INTO anime_original_author(anime_id, original_author_id,  created_at, updated_at) VALUES (1, 1, '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');
INSERT INTO anime_original_author(anime_id, original_author_id,  created_at, updated_at) VALUES (2, 2, '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');
INSERT INTO anime_original_author(anime_id, original_author_id,  created_at, updated_at) VALUES (3, 3, '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');

INSERT INTO voice_actor(name, created_at, updated_at) VALUES ('하나에 나츠키', '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');
INSERT INTO voice_actor(name, created_at, updated_at) VALUES ('시모노 히로', '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');
INSERT INTO voice_actor(name, created_at, updated_at) VALUES ('마츠오카 요시츠구', '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');

INSERT INTO anime_voice_actor(anime_id, voice_actor_id, part, created_at, updated_at) VALUES (1, 1, '카마도 탄지로', '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');
INSERT INTO anime_voice_actor(anime_id, voice_actor_id, part, created_at, updated_at) VALUES (1, 2, '아가츠마 젠이츠', '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');
INSERT INTO anime_voice_actor(anime_id, voice_actor_id, part, created_at, updated_at) VALUES (1, 3, '하바시라 이노스케', '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');
INSERT INTO anime_voice_actor(anime_id, voice_actor_id, part, created_at, updated_at) VALUES (2, 1, '카마도 탄지로', '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');
INSERT INTO anime_voice_actor(anime_id, voice_actor_id, part, created_at, updated_at) VALUES (2, 2, '아가츠마 젠이츠', '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');
INSERT INTO anime_voice_actor(anime_id, voice_actor_id, part, created_at, updated_at) VALUES (3, 1, '카마도 탄지로', '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');
INSERT INTO anime_voice_actor(anime_id, voice_actor_id, part, created_at, updated_at) VALUES (3, 3, '하바시라 이노스케', '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');

INSERT INTO studio(name, created_at, updated_at) VALUES ('ufortable', '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');
INSERT INTO studio(name, created_at, updated_at) VALUES ('WIT STUDIO', '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');
INSERT INTO studio(name, created_at, updated_at) VALUES ('CloverWorks', '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');

INSERT INTO anime_studio(anime_id, studio_id, created_at, updated_at) VALUES (1, 1, '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');
INSERT INTO anime_studio(anime_id, studio_id, created_at, updated_at) VALUES (2, 2, '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');
INSERT INTO anime_studio(anime_id, studio_id, created_at, updated_at) VALUES (3, 3, '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');

INSERT INTO genre(name, created_at, updated_at) VALUES ('판타지', '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');
INSERT INTO genre(name, created_at, updated_at) VALUES ('액션', '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');
INSERT INTO genre(name, created_at, updated_at) VALUES ('코메디', '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');

INSERT INTO anime_genre(anime_id, genre_id, created_at, updated_at) VALUES (1, 1, '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');
INSERT INTO anime_genre(anime_id, genre_id, created_at, updated_at) VALUES (1, 2, '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');
INSERT INTO anime_genre(anime_id, genre_id, created_at, updated_at) VALUES (2, 1, '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');
INSERT INTO anime_genre(anime_id, genre_id, created_at, updated_at) VALUES (2, 2, '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');
INSERT INTO anime_genre(anime_id, genre_id, created_at, updated_at) VALUES (3, 1, '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');
INSERT INTO anime_genre(anime_id, genre_id, created_at, updated_at) VALUES (3, 3, '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859');

INSERT INTO bookmark(anime_id, created_at, member_id) VALUES(1, '2023-10-10 21:05:31.859', 1);
INSERT INTO bookmark(anime_id, created_at, member_id) VALUES(2, '2023-10-11 21:05:31.859', 1);
INSERT INTO bookmark(anime_id, created_at, member_id) VALUES(3, '2023-10-12 21:05:31.859', 1);

INSERT INTO star_rating(score, anime_id, created_at, member_id, updated_at) VALUES(1, 1, '2023-10-10 21:05:31.859', 1, '2023-10-10 21:05:31.859');
INSERT INTO star_rating(score, anime_id, created_at, member_id, updated_at) VALUES(2, 2, '2023-10-11 21:05:31.859', 1, '2023-10-11 21:05:31.859');
INSERT INTO star_rating(score, anime_id, created_at, member_id, updated_at) VALUES(2, 1, '2023-10-12 21:05:31.859', 2, '2023-10-12 21:05:31.859');
INSERT INTO star_rating(score, anime_id, created_at, member_id, updated_at) VALUES(3, 1, '2023-10-13 21:05:31.859', 3, '2023-10-13 21:05:31.859');

INSERT INTO short_review(has_spoiler, anime_id, created_at, member_id, updated_at, content) VALUES(0, 1, '2023-10-10 21:05:31.859', 1, '2023-10-10 21:05:31.859', '최고');
INSERT INTO short_review(has_spoiler, anime_id, created_at, member_id, updated_at, content) VALUES(0, 2, '2023-10-10 21:05:31.859', 1, '2023-10-10 21:05:31.859', '최고');
INSERT INTO short_review(has_spoiler, anime_id, created_at, member_id, updated_at, content) VALUES(0, 3, '2023-10-10 21:05:31.859', 1, '2023-10-10 21:05:31.859', '최고');
INSERT INTO short_review(has_spoiler, anime_id, created_at, member_id, updated_at, content) VALUES(0, 1, '2023-10-10 21:05:31.859', 2, '2023-10-10 21:05:31.859', '최고');
INSERT INTO short_review(has_spoiler, anime_id, created_at, member_id, updated_at, content) VALUES(0, 2, '2023-10-11 21:05:31.859', 2, '2023-10-11 21:05:31.859', '힐링');
INSERT INTO short_review(has_spoiler, anime_id, created_at, member_id, updated_at, content) VALUES(0, 3, '2023-10-12 21:05:31.859', 2, '2023-10-13 21:05:31.859', '힐링');

INSERT INTO short_review_like(created_at, member_id, short_review_id, updated_at) VALUES('2023-10-10 21:05:31.859', 1, 1, '2023-10-10 21:05:31.859');
INSERT INTO short_review_like(created_at, member_id, short_review_id, updated_at) VALUES('2023-10-11 21:05:31.859', 1, 2, '2023-10-11 21:05:31.859');

INSERT INTO attraction_point(created_at, member_id, anime_id, attraction_element, updated_at) VALUES('2023-11-10 21:05:31.859', 2, 1, 'DRAWING','2023-11-15 21:05:31.859');
INSERT INTO attraction_point(created_at, member_id, anime_id, attraction_element, updated_at) VALUES('2023-11-10 21:05:31.859', 2, 1, 'STORY','2023-11-15 21:05:31.859');