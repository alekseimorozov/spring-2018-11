delete
from PUBLIC.AUTHOR_TO_BOOK_MAP;
DELETE
FROM PUBLIC.BOOKS;
DELETE
FROM PUBLIC.AUTHORS;
DELETE
FROM PUBLIC.GENRES;
INSERT INTO PUBLIC.GENRES (ID, NAME)
VALUES (1, 'ДЕТСКАЯ');
INSERT INTO PUBLIC.GENRES (ID, NAME)
VALUES (2, 'САТИРА');
INSERT INTO PUBLIC.AUTHORS (ID, NAME)
VALUES (1, 'НИКОЛАЙ НОСОВ');
INSERT INTO PUBLIC.AUTHORS (ID, NAME)
VALUES (2, 'ИЛЬЯ ИЛЬФ');
INSERT INTO PUBLIC.AUTHORS (ID, NAME)
VALUES (3, 'ЕВГЕНИЙ ПЕТРОВ');
INSERT INTO PUBLIC.AUTHORS (ID, NAME)
VALUES (4, 'КАРНЕЙ ЧУКОВСКИЙ');
INSERT INTO PUBLIC.BOOKS (ID, NAME, GENRE_ID)
VALUES (1, 'ЗАТЕЙНИКИ', 1);
INSERT INTO PUBLIC.BOOKS (ID, NAME, GENRE_ID)
VALUES (2, 'ЗОЛОТОЙ ТЕЛЕНОК', 2);
INSERT INTO PUBLIC.AUTHOR_TO_BOOK_MAP(AUTHOR_ID, BOOK_ID)
VALUES (1, 1);
INSERT INTO PUBLIC.AUTHOR_TO_BOOK_MAP(AUTHOR_ID, BOOK_ID)
VALUES (2, 2);
INSERT INTO PUBLIC.AUTHOR_TO_BOOK_MAP(AUTHOR_ID, BOOK_ID)
VALUES (3, 2);

