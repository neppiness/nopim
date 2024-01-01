INSERT INTO USER (id, name, password, authority) VALUES (1, 'scsc3204', 'Neppiness12!', 'ADMIN');
INSERT INTO USER (id, name, password, authority) VALUES (2, '0414kjh', 'Neppiness12!', 'MEMBER');

INSERT INTO COMPANY (id, name, region, country) VALUES (1, '원티드랩', '서울', '대한민국');
INSERT INTO COMPANY (id, name, region, country) VALUES (2, '원티드코리아', '부산', '대한민국');
INSERT INTO COMPANY (id, name, region, country) VALUES (3, '네이버', '판교', '대한민국');
INSERT INTO COMPANY (id, name, region, country) VALUES (4, '카카오', '판교', '대한민국');
INSERT INTO COMPANY (id, name, region, country) VALUES (5, '네이버클라우드 판교오피스', '판교', '대한민국');
INSERT INTO COMPANY (id, name, region, country) VALUES (6, '스노우', '판교', '대한민국');

INSERT INTO JOB (id, company_id, position, bounty, stack, description, status)
VALUES (1, 2, '프론트엔드 개발자', 500000, 'javascript', '원티드코리아에서 프론트엔드 개발자를 채용합니다.', 'OPEN');
INSERT INTO JOB (id, company_id, position, bounty, stack, description, status)
VALUES (2, 3, 'Django 백엔드 개발자', 1000000, 'Django', '네이버에서 백엔드 개발자를 채용합니다.', 'OPEN');
INSERT INTO JOB (id, company_id, position, bounty, stack, description, status)
VALUES (3, 4, 'Django 백엔드 개발자', 300000, 'Django', '카카오에서 Django 백엔드 개발자를 채용합니다.', 'OPEN');
