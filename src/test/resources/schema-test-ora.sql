--
-- 회원
--
DROP TABLE IF EXISTS MEMBER;

CREATE TABLE MEMBER(
    MBR_ID      VARCHAR(10)     NOT NULL    COMMENT '회원ID'
    , NAME        VARCHAR(100)                COMMENT '회원명'
    , PRIMARY KEY (MBR_ID)
    );