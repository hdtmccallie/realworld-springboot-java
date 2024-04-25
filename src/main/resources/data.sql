
insert into users (id, name, email, password)
select 1, 'Dave', 'dave@example.com', 'password';

insert into articles (id, author_id, title, slug, description, body)
select 1, 1, 'Test Article', 'test', 'Just a test', 'It really is a test';
