insert into "role" ("created_at_ad", "updated_at_ad", "name", "id") values (null, null, 'ADMIN', 1);
insert into "role" ("created_at_ad", "updated_at_ad", "name", "id") values (null, null, 'MANAGER', 2);
insert into "role" ("created_at_ad", "updated_at_ad", "name", "id") values (null, null, 'ASSISTANT', 3);
insert into "role" ("created_at_ad", "updated_at_ad", "name", "id") values (null, null, 'USER', 4);

insert into "users" ("created_at_ad",
                     "updated_at_ad",
                     "email",
                     "is_active",
                     "password",
                     "username",
                     "user_id")
values(null,null,
       'admin@gmail.com',
       TRUE,
       '$2a$10$u9LoR5pZlwl1wXMbJXccOOQLp0NPjGCBM40TrCmV4TkOmNH.efrlO',
       'admin',
       1);

select * from "users" where username='admin';
select * from "role" where name='ADMIN';

insert into user_roles (user_user_id, roles_id) values (1,1);
