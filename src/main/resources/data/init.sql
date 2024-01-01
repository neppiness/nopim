INSERT INTO USER (name, password) VALUES ('김정현', 'Neppiness12!');

INSERT INTO COMPANY (name, region, country) VALUES ('원티드랩', '서울', '대한민국');
INSERT INTO COMPANY (name, region, country) VALUES ('원티드코리아', '부산', '대한민국');
INSERT INTO COMPANY (name, region, country) VALUES ('네이버', '판교', '대한민국');
INSERT INTO COMPANY (name, region, country) VALUES ('카카오', '판교', '대한민국');

INSERT INTO JOB (company_id, position, bounty, stack, description, status)
VALUES (2, '프론트엔드 개발자', 500000, 'javascript', '원티드코리아에서 프론트엔드 개발자를 채용합니다.', 'OPEN');
INSERT INTO JOB (company_id, position, bounty, stack, description, status)
VALUES (3, 'Django 백엔드 개발자', 1000000, 'Django', '네이버에서 백엔드 개발자를 채용합니다.', 'OPEN');
INSERT INTO JOB (company_id, position, bounty, stack, description, status)
VALUES (4, 'Django 백엔드 개발자', 300000, 'Django', '카카오에서 Django 백엔드 개발자를 채용합니다.', 'OPEN');