package gtu.spring.transcation;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 侵入式的寫法
 */
public class TransactionSample {

    static class ForumService {
        private ForumDao forumDao;

        private TransactionTemplate template;

        public void addForum(final Forum forum) {
            template.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus arg0) {
                    forumDao.addForum(forum);
                }
            });
        }
    }

    static class Forum {
        String topic;
    }

    static class ForumDao {
        void addForum(Forum forum) {
            //TODO...
        }
    }
}
