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
