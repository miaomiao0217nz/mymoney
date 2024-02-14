insert into users(id, email, first_name , last_name, password , user_name, role) values(-1,'sophie@gmail.com','Sophie','Xu','$2a$10$vrt7raX2aoxiG72aD9brbOJOxJMcJD6avu9gXILSIexfg2rSq2us.','sophie','ADMIN');
insert into users(id, email, first_name , last_name, password , user_name, role) values(-2,'testuser@gmail.com','Test','User','$2a$10$vrt7raX2aoxiG72aD9brbOJOxJMcJD6avu9gXILSIexfg2rSq2us.','test','USER');

ALTER USER sa SET PASSWORD 'this is a test';