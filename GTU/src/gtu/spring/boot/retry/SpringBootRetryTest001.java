package gtu.spring.boot.retry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * dependencies {
        compile 'org.springframework.retry:spring-retry'
    }
 *
 *
 *  Retryable 就是 Spring Retry 提供的註解

    當捕捉到 NoResultException 的時候會自動進行重試
    
    maxAttempts 表示最多執行三次
    
    backoff 表示間隔，當捕捉到錯誤時，停多少秒後再重試
    
    @Recover 則是定義該錯誤的處理,只能寫在同一個 Class 裡面喔,
    當重試次數超過 maxAttempts 時候會跳到對應的 Recover 來處理,
    如果原本的功能有 return 的話, 記得 Recover 也要有 return 喔

 */
@Slf4j
@Service
public class SpringBootRetryTest001 {
    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    @Retryable(include = {NoResultException.class},
            maxAttempts = 3,
            backoff = @Backoff(value = 2000))
    public Book getBook() {
        int count = atomicInteger.incrementAndGet();
        log.info("count = " + count);
        if (count < 5) {
            throw new NoResultException();
        } else {
            return new Book();
        }
    }

    @Recover
    public Book recover(NoResultException e) {
        log.info("get NoResultException & return null");
        return null;
    }
    
    private class Book {
    }
}
