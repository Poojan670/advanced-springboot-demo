alter table "role" add constraint "UK_8sewwnpamngi6b1dwaa88askk" unique ("name");

alter table "users" add constraint "UK_6dotkott2kjsp8vw4d0m25fb7" unique ("email");

alter table "users" add constraint "UK_r43af9ap4edm43mmtq01oddj6" unique ("username");

alter table "user_roles" add constraint "FKo2rmqkw5pucpp44p39quu5al5" foreign key ("roles_id")
    references "role";

alter table "user_roles" add constraint "FKd3ge58571ljrqdi55tjyu8y4q" foreign key ("user_user_id")
    references "users";

alter table "confirm_token" add constraint "FK5m1lxn4cxthf6t2s8h6i85dky" foreign key ("users")
    references "users";

alter table "user_profile" add constraint "FKr9fkd6uajmv7d44rrpvf9xsmw" foreign key ("user_id")
    references "users"