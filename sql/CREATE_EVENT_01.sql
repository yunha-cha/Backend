-- 매시 자정에 실행되는 이벤트
CREATE EVENT complete_delete_events
    ON SCHEDULE EVERY 1 DAY
        STARTS TIMESTAMP(CURDATE() + INTERVAL 1 DAY) -- 매일 자정에 시작
    DO
    BEGIN
        -- event_delete_status가 'T'이고 event_delete_time이 현재날짜보다 7일 이상인 데이터 업데이트
        UPDATE tbl_event_options
        SET event_delete_status = 'Y',
            event_delete_time = NULL
        WHERE event_delete_status = 'T'
          AND event_delete_time <= NOW() - INTERVAL 7 DAY;
    END;