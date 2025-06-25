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
