CREATE TABLE members (
                                id bigserial NOT NULL,
                                email varchar(255) NOT NULL,
                                "password" varchar(255) NOT NULL,
                                "name" varchar(255) NOT NULL,
                                phone_number varchar(20) NULL,
                                "role" varchar(20) NULL,
                                created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                updated_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                CONSTRAINT member_email_key UNIQUE (email),
                                CONSTRAINT member_pkey PRIMARY KEY (id)
);

CREATE TABLE member_clubs (
                                     id bigserial NOT NULL,
                                     member_id int8 NOT NULL,
                                     club_id int8 NOT NULL,
                                     member_role varchar(50) NULL,
                                     status varchar(50) NULL,
                                     CONSTRAINT member_clubs_pkey PRIMARY KEY (id)
);

CREATE TABLE clubs (
                              id bigserial NOT NULL,
                              "name" varchar(255) NOT NULL,
                              sport_type varchar(100) NULL,
                              description text NULL,
                              status varchar(100) NULL,
                              CONSTRAINT clubs_pkey PRIMARY KEY (id)
);

CREATE TABLE club_apply_requests (
                                            id bigserial NOT NULL,
                                            member_id int8 NOT NULL,
                                            club_id int8 NOT NULL,
                                            status varchar(20) NOT NULL,
                                            reason text NULL,
                                            created_at timestamp NOT NULL DEFAULT now(),
                                            handled_at timestamp NULL,
                                            handled_by int8 NULL,
                                            CONSTRAINT club_join_applys_pkey PRIMARY KEY (id)
);

CREATE TABLE games (
                              id bigserial NOT NULL,
                              title varchar(255) NULL,
                              start_time timestamp NULL,
                              end_time timestamp NULL,
                              host_club_id int8 NULL,
                              guest_club_id int8 NULL,
                              host_score int4 NULL,
                              guest_score int4 NULL,
                              status varchar(50) NULL,
                              sport_type varchar(100) NULL,
                              CONSTRAINT games_pkey PRIMARY KEY (id)
);

CREATE TABLE participations (
                                       id bigserial NOT NULL,
                                       member_id int8 NOT NULL,
                                       game_id int8 NOT NULL,
                                       club_id int8 NOT NULL,
                                       CONSTRAINT participations_pkey PRIMARY KEY (id)
);

CREATE TABLE game_records (
                                     id bigserial NOT NULL,
                                     game_id int8 NOT NULL,
                                     member_id int8 NOT NULL,
                                     club_id int8 NOT NULL,
                                     created_at timestamp NULL,
                                     updated_at timestamp NULL,
                                     CONSTRAINT game_records_pkey PRIMARY KEY (id)
);

CREATE TABLE basketball_records (
                                           id int8 NOT NULL,
                                           points int4 NULL DEFAULT 0,
                                           assists int4 NULL DEFAULT 0,
                                           rebounds int4 NULL DEFAULT 0,
                                           steals int4 NULL DEFAULT 0,
                                           blocks int4 NULL DEFAULT 0,
                                           play_time int4 NULL DEFAULT 0,
                                           fouls int4 NULL DEFAULT 0,
                                           CONSTRAINT basketball_records_pkey PRIMARY KEY (id)
);

INSERT INTO games
(id, title, start_time, end_time, host_club_id, guest_club_id, host_score, guest_score, status, sport_type)
VALUES (3, '테스트경기2', '2025-06-12 14:00:00.000', NULL, 6, 19, 0, 0, 'IN_PROGRESS', 'BASKETBALL');

INSERT INTO members
(id, email, "password", "name", phone_number, "role", created_at, updated_at)
VALUES (13, 'baltol@naver.com', '$2a$10$vSuZWASg/3YOMhDUiVSgg.x1FHFu1/udXiwp5AUvDtCUE5rzcRDaO', '백인엽', '010-2222-3333', 'ROLE_USER', '2025-05-10 15:29:38.437', '2025-05-10 15:29:38.437');

INSERT INTO members
(id, email, "password", "name", phone_number, "role", created_at, updated_at)
VALUES (28, 'test@flab.com', '$2a$10$nMNVyGUI53od3WG3u/MS7Ol2uIS1ZCivIcqM8lgVGOx4dI1qB04ay', '테스트', '010-1111-2222', 'ROLE_USER', '2025-05-14 23:12:51.935', '2025-05-14 23:12:51.935');

INSERT INTO members
(id, email, "password", "name", phone_number, "role", created_at, updated_at)
VALUES (29, 'club@flab.com', '$2a$10$fb8HITDn5u92FIjV4JQlguchEO06P18U7E5qoRxeu3aBPl/Bid76O', '김클럽', '010-1111-2222', 'ROLE_USER', '2025-05-18 16:50:19.434', '2025-05-18 16:50:19.434');

INSERT INTO members
(id, email, "password", "name", phone_number, "role", created_at, updated_at)
VALUES (30, 'test1@flab.com', '$2a$10$Cn/ex7H.CGf3/dyhnlh.QeULgJiClbjm8VziCBzBFjB9N.glR8G.2', '테스트일', '010-1111-2222', 'ROLE_USER', '2025-06-11 23:41:35.765', '2025-06-11 23:41:35.765');

INSERT INTO members
(id, email, "password", "name", phone_number, "role", created_at, updated_at)
VALUES (31, 'test2@flab.com', '$2a$10$5uBk1HKAHadNOzOjmSwOtOu1d0hHtT5JTe8jzhENADeFANcUv12/O', '테스트이', '010-1111-2222', 'ROLE_USER', '2025-06-11 23:41:43.671', '2025-06-11 23:41:43.671');

INSERT INTO members
(id, email, "password", "name", phone_number, "role", created_at, updated_at)
VALUES (32, 'test3@flab.com', '$2a$10$ACjNd2Kf/4GXT.WORtSXRO/DLoh5zImPu2TmEbnyt6wI8wSH4TDru', '테스트삼', '010-1111-2222', 'ROLE_USER', '2025-06-11 23:41:51.309', '2025-06-11 23:41:51.309');

INSERT INTO members
(id, email, "password", "name", phone_number, "role", created_at, updated_at)
VALUES (33, 'test4@flab.com', '$2a$10$6i97B7iGjHC3Dd.DZlA1Eu74hA5MQ2mJA1o6HRzb1zr7Jlu5TyGkC', '테스트사', '010-1111-2222', 'ROLE_USER', '2025-06-11 23:41:58.805', '2025-06-11 23:41:58.805');

INSERT INTO members
(id, email, "password", "name", phone_number, "role", created_at, updated_at)
VALUES (34, 'test5@flab.com', '$2a$10$iJtWpiOxBmATxdksnbQoUeUZO7L1CWquBeu6sooQ3VdHlwxh3bFEa', '테스트오', '010-1111-2222', 'ROLE_USER', '2025-06-11 23:42:05.149', '2025-06-11 23:42:05.149');

INSERT INTO members
(id, email, "password", "name", phone_number, "role", created_at, updated_at)
VALUES (35, 'test6@flab.com', '$2a$10$YTAIOT7ETfYbdTpCpuABYu1VK3.QKtruoD4i2.Ba2uJ3mv2MNUjum', '테스트육', '010-1111-2222', 'ROLE_USER', '2025-06-11 23:42:11.834', '2025-06-11 23:42:11.834');

INSERT INTO members
(id, email, "password", "name", phone_number, "role", created_at, updated_at)
VALUES (36, 'test7@flab.com', '$2a$10$SZ.vk.NW4QgH.qOX/getoe71ZIMvdJUji0l2m3boqcZ5KffooOphK', '테스트칠', '010-1111-2222', 'ROLE_USER', '2025-06-11 23:42:18.432', '2025-06-11 23:42:18.432');

INSERT INTO members
(id, email, "password", "name", phone_number, "role", created_at, updated_at)
VALUES (37, 'test8@flab.com', '$2a$10$rfwJBwXpkvM.1RA.xI/2I.sjnwvSHNjdPG9lPiYVh1EAeJeahCAvi', '테스트팔', '010-1111-2222', 'ROLE_USER', '2025-06-11 23:42:26.371', '2025-06-11 23:42:26.371');

INSERT INTO members
(id, email, "password", "name", phone_number, "role", created_at, updated_at)
VALUES (38, 'test9@flab.com', '$2a$10$Sr9TQcczr/F2E1NyOgxKJeJNLVd8mqFlSTFPfOZ2mGE5qYYq2e4ZW', '테스트구', '010-1111-2222', 'ROLE_USER', '2025-06-11 23:42:35.906', '2025-06-11 23:42:35.906');

INSERT INTO members
(id, email, "password", "name", phone_number, "role", created_at, updated_at)
VALUES (39, 'test10@flab.com', '$2a$10$AzykpBQYdnNQMLmINLKwK.aa2h5HpgnWLDMpG7/T/Iw/HXgyOB2na', '테스트십', '010-1111-2222', 'ROLE_USER', '2025-06-11 23:42:42.549', '2025-06-11 23:42:42.549');


INSERT INTO participations
(id, member_id, game_id, club_id)
VALUES (43, 29, 3, 6);

INSERT INTO participations
(id, member_id, game_id, club_id)
VALUES (44, 31, 3, 6);

INSERT INTO participations
(id, member_id, game_id, club_id)
VALUES (45, 32, 3, 6);

INSERT INTO participations
(id, member_id, game_id, club_id)
VALUES (46, 33, 3, 6);

INSERT INTO participations
(id, member_id, game_id, club_id)
VALUES (47, 34, 3, 6);

INSERT INTO participations
(id, member_id, game_id, club_id)
VALUES (48, 30, 3, 19);

INSERT INTO participations
(id, member_id, game_id, club_id)
VALUES (49, 36, 3, 19);

INSERT INTO participations
(id, member_id, game_id, club_id)
VALUES (50, 37, 3, 19);

INSERT INTO participations
(id, member_id, game_id, club_id)
VALUES (51, 38, 3, 19);

INSERT INTO participations
(id, member_id, game_id, club_id)
VALUES (52, 39, 3, 19);


INSERT INTO game_records
(id, game_id, member_id, club_id, created_at, updated_at)
VALUES (41, 3, 29, 6, '2025-06-13 22:30:04.279', NULL);

INSERT INTO game_records
(id, game_id, member_id, club_id, created_at, updated_at)
VALUES (42, 3, 31, 6, '2025-06-13 22:30:04.286', NULL);

INSERT INTO game_records
(id, game_id, member_id, club_id, created_at, updated_at)
VALUES (43, 3, 32, 6, '2025-06-13 22:30:04.288', NULL);

INSERT INTO game_records
(id, game_id, member_id, club_id, created_at, updated_at)
VALUES (44, 3, 33, 6, '2025-06-13 22:30:04.289', NULL);

INSERT INTO game_records
(id, game_id, member_id, club_id, created_at, updated_at)
VALUES (45, 3, 34, 6, '2025-06-13 22:30:04.291', NULL);

INSERT INTO game_records
(id, game_id, member_id, club_id, created_at, updated_at)
VALUES (46, 3, 30, 19, '2025-06-13 22:30:04.293', NULL);

INSERT INTO game_records
(id, game_id, member_id, club_id, created_at, updated_at)
VALUES (47, 3, 36, 19, '2025-06-13 22:30:04.294', NULL);

INSERT INTO game_records
(id, game_id, member_id, club_id, created_at, updated_at)
VALUES (48, 3, 37, 19, '2025-06-13 22:30:04.295', NULL);

INSERT INTO game_records
(id, game_id, member_id, club_id, created_at, updated_at)
VALUES (49, 3, 38, 19, '2025-06-13 22:30:04.296', NULL);

INSERT INTO game_records
(id, game_id, member_id, club_id, created_at, updated_at)
VALUES (50, 3, 39, 19, '2025-06-13 22:30:04.297', NULL);


INSERT INTO basketball_records
(id, points, assists, rebounds, steals, blocks, play_time, fouls)
VALUES (42, 0, 0, 0, 0, 0, 0, 0);

INSERT INTO basketball_records
(id, points, assists, rebounds, steals, blocks, play_time, fouls)
VALUES (43, 0, 0, 0, 0, 0, 0, 0);

INSERT INTO basketball_records
(id, points, assists, rebounds, steals, blocks, play_time, fouls)
VALUES (44, 0, 0, 0, 0, 0, 0, 0);

INSERT INTO basketball_records
(id, points, assists, rebounds, steals, blocks, play_time, fouls)
VALUES (45, 0, 0, 0, 0, 0, 0, 0);

INSERT INTO basketball_records
(id, points, assists, rebounds, steals, blocks, play_time, fouls)
VALUES (47, 0, 0, 0, 0, 0, 0, 0);

INSERT INTO basketball_records
(id, points, assists, rebounds, steals, blocks, play_time, fouls)
VALUES (48, 0, 0, 0, 0, 0, 0, 0);

INSERT INTO basketball_records
(id, points, assists, rebounds, steals, blocks, play_time, fouls)
VALUES (49, 0, 0, 0, 0, 0, 0, 0);

INSERT INTO basketball_records
(id, points, assists, rebounds, steals, blocks, play_time, fouls)
VALUES (50, 0, 0, 0, 0, 0, 0, 0);

INSERT INTO basketball_records
(id, points, assists, rebounds, steals, blocks, play_time, fouls)
VALUES (41, 0, 0, 0, 0, 0, 0, 0);

INSERT INTO basketball_records
(id, points, assists, rebounds, steals, blocks, play_time, fouls)
VALUES (46, 0, 0, 0, 0, 0, 0, 0);


