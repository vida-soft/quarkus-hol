insert into Users (version, email, firstName, lastName, password, userName, isRegular, salary, DTYPE, id)
values (0, 'cj@apperture-science.io', 'Cave', 'Johnson', 'cj_pass', 'cj_123', true, 10000, 'Author', 1),
       (0, 'jane@apperture-science.io', 'Jane', 'Doe', 'jd_pass', 'jd_123', true, 10000, 'Author', 2);


insert into ARTICLE (ID, VERSION, CONTENT, PUBLISHDATE, TITLE, AUTHOR_ID)
values (3, 0, 'The quick brown fox runs over the lazy dog.', '2022-01-12', 'Article for the soul.', 1),
       (4, 0, 'This is an article by the same author, who created Ipsum Lorem', '2022-02-12', 'The aitor that created',
        1),
       (5, 0, 'This is how I got my hands into Java long time ago. Long article here...', '2020-01-10',
        'The way I became Java developer', 2),
       (6, 0, 'This is my extreme enjoyment of Quarkus, written in an article', '2022-09-13',
        'I love Quarkus and Quarkus loves me back', 2);


alter sequence HIBERNATE_SEQUENCE restart with 7;