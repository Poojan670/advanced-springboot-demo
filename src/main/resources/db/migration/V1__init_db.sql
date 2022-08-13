create sequence "user_sequence" start with 1 increment by 1;
create sequence "token_sequence" start with 1 increment by 1;
create sequence "user_details_sequence" start with 1 increment by 1;

create table "users" (
                 "user_id" int8 not null,
                 "created_at_ad" timestamp,
                 "updated_at_ad" timestamp,
                 "email" varchar(50) not null,
                 "is_active" boolean,
                 "password" varchar(255),
                 "username" varchar(30) not null,
                 primary key ("user_id")
);


create table "role" (
            "id" int8 not null,
            "created_at_ad" timestamp,
            "updated_at_ad" timestamp,
            "name" varchar(30) not null,
            primary key ("id")
);

create table "confirm_token" (
             "id" int8 not null,
             "confirmed_at" timestamp,
             "created_at" timestamp not null,
             "expires_at" timestamp not null,
             "token" varchar(255) not null,
             "users" int8 not null,
             primary key ("id")
);


create table "user_roles" (
          "user_user_id" int8 not null,
          "roles_id" int8 not null
);

create table "user_profile" (
                "user_details_id" int8 not null,
                "address" varchar(255),
                "age" bytea,
                "created_at_ad" timestamp,
                "updated_at_ad" timestamp,
                "date_of_birth" date not null,
                "first_name" varchar(50) not null,
                "full_name" varchar(255),
                "last_name" varchar(50) not null,
                "middle_name" varchar(50),
                "photo" varchar(255),
                "user_id" int8,
                primary key ("user_details_id")
);
