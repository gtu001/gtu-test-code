package gtu.hibernate;

import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;

/**
 * @author 2012/1/8
 * 
 *         获取 Hibernate InvalidStateException 异常信息的详情
 */
public class InvalidStateExceptionInfo {

    public static void main(String[] args) {
        try {
            // assignedQuestionnaireManager.save(ongoingAssignedQuestionnaire);
        } catch (InvalidStateException v) {
            InvalidValue[] invalid = v.getInvalidValues();
            for (int i = 0; i < invalid.length; ++i) {
                InvalidValue bad = invalid[i];
                System.out.println("error on:" + bad.getPropertyPath() + ":" + bad.getPropertyName() + ":"
                        + bad.getMessage());
            }
        }
    }
}
