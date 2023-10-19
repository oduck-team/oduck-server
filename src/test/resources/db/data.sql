INSERT INTO member(created_at, updated_at, login_type, `role`) VALUES('2023-10-10 21:05:31.859', '2023-10-10 21:05:31.859', 'LOCAL', 'MEMBER');
INSERT INTO auth_local(created_at, member_id, updated_at, password, email) VALUES('2023-10-10 21:05:31.859', 1, '2023-10-10 21:05:31.859', '{bcrypt}$2a$10$C0G6uQz.MzfsSH7BZFRBz.MPBmFSV2zAloqqBIwaUpxnmMgCQK..i', 'oduckdmin@gmail.com');
INSERT INTO member_profile (created_at, member_id, updated_at, name, info, thumbnail) VALUES('2023-10-10 21:05:31.859', 1, '2023-10-10 21:05:31.859', 'admin', 'admin info', 'http://thumbnail.com');

INSERT INTO member(created_at, updated_at, login_type, `role`) VALUES('2023-10-11 21:05:31.859', '2023-10-11 21:05:31.859', 'LOCAL', 'MEMBER');
INSERT INTO auth_local(created_at, member_id, updated_at, password, email) VALUES('2023-10-11 21:05:31.859', 2, '2023-10-11 21:05:31.859', '{bcrypt}$2a$10$C0G6uQz.MzfsSH7BZFRBz.MPBmFSV2zAloqqBIwaUpxnmMgCQK..i', 'john@gmail.com');
INSERT INTO member_profile (created_at, member_id, updated_at, name, info, thumbnail) VALUES('2023-10-11 21:05:31.859', 2, '2023-10-11 21:05:31.859', 'john', 'john info', 'http://thumbnail.com');

INSERT INTO member(created_at, updated_at, login_type, `role`) VALUES('2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859', 'LOCAL', 'MEMBER');
INSERT INTO auth_local(created_at, member_id, updated_at, password, email) VALUES('2023-10-12 21:05:31.859', 3, '2023-10-12 21:05:31.859', '{bcrypt}$2a$10$C0G6uQz.MzfsSH7BZFRBz.MPBmFSV2zAloqqBIwaUpxnmMgCQK..i', 'david@gmail.com');
INSERT INTO member_profile (created_at, member_id, updated_at, name, info, thumbnail) VALUES('2023-10-12 21:05:31.859', 3, '2023-10-12 21:05:31.859', 'david', 'david info', 'http://thumbnail.com');

INSERT INTO anime(episode_count, is_released, created_at, updated_at, title, thumbnail, broadcast_type, quarter, rating, status, summary, release_year)
VALUES(0, 1, '2023-10-10 21:05:31.859', '2023-10-10 21:05:31.859', '강연금', 'http://thumbnail.com', 'TVA', 'Q1', 'ALL', 'ONGOING', '1', 2009);
INSERT INTO anime(episode_count, is_released, created_at, updated_at, title, thumbnail, broadcast_type, quarter, rating, status, summary, release_year)
VALUES(0, 1, '2023-10-11 21:05:31.859', '2023-10-11 21:05:31.859', '스파패', 'http://thumbnail.com', 'TVA', 'Q2', 'ALL', 'ONGOING', '2', 2023);
INSERT INTO anime(episode_count, is_released, created_at, updated_at, title, thumbnail, broadcast_type, quarter, rating, status, summary, release_year)
VALUES(0, 1, '2023-10-12 21:05:31.859', '2023-10-12 21:05:31.859', '귀멸칼날', 'http://thumbnail.com', 'TVA', 'Q3', 'ALL', 'ONGOING', '3', 2022);

INSERT INTO bookmark(anime_id, created_at, member_id) VALUES(1, '2023-10-10 21:05:31.859', 1);
INSERT INTO bookmark(anime_id, created_at, member_id) VALUES(2, '2023-10-11 21:05:31.859', 1);
INSERT INTO bookmark(anime_id, created_at, member_id) VALUES(3, '2023-10-12 21:05:31.859', 1);

INSERT INTO star_rating(score, anime_id, created_at, member_id, updated_at) VALUES(1, 1, '2023-10-10 21:05:31.859', 1, '2023-10-10 21:05:31.859');
INSERT INTO star_rating(score, anime_id, created_at, member_id, updated_at) VALUES(2, 2, '2023-10-11 21:05:31.859', 1, '2023-10-11 21:05:31.859');
INSERT INTO star_rating(score, anime_id, created_at, member_id, updated_at) VALUES(2, 1, '2023-10-12 21:05:31.859', 2, '2023-10-12 21:05:31.859');
INSERT INTO star_rating(score, anime_id, created_at, member_id, updated_at) VALUES(3, 1, '2023-10-13 21:05:31.859', 3, '2023-10-13 21:05:31.859');

INSERT INTO short_review(has_spoiler, anime_id, created_at, member_id, updated_at, content) VALUES(0, 1, '2023-10-10 21:05:31.859', 1, '2023-10-10 21:05:31.859', '최고');
INSERT INTO short_review(has_spoiler, anime_id, created_at, member_id, updated_at, content) VALUES(0, 2, '2023-10-11 21:05:31.859', 1, '2023-10-11 21:05:31.859', '힐링');

INSERT INTO short_review_like(created_at, member_id, short_review_id, updated_at) VALUES('2023-10-10 21:05:31.859', 1, 1, '2023-10-10 21:05:31.859');
INSERT INTO short_review_like(created_at, member_id, short_review_id, updated_at) VALUES('2023-10-11 21:05:31.859', 1, 1, '2023-10-11 21:05:31.859');