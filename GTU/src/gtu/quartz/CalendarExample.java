package gtu.quartz;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.calendar.AnnualCalendar;
import org.quartz.impl.triggers.SimpleTriggerImpl;

/**
 * 避開節日
 */
public class CalendarExample {

    public static class SimpleJob implements Job {
        @Override
        public void execute(JobExecutionContext jobCtx) throws JobExecutionException {
            System.out.println(((SimpleTriggerImpl) jobCtx.getTrigger()).getName() + " triggered. time is : " + new Date());
        }
    }

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws SchedulerException {
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler scheduler = sf.getScheduler();

        AnnualCalendar holidays = new AnnualCalendar();

        Calendar laborDay = new GregorianCalendar();//五一勞動節
        laborDay.add(Calendar.MONTH, 5);
        laborDay.add(Calendar.DATE, 1);
        holidays.setDayExcluded(laborDay, true);//排除的日期 ,若設為true則包含

        Calendar nationalDay = new GregorianCalendar();//國慶
        nationalDay.add(Calendar.MONTH, 10);
        nationalDay.add(Calendar.DATE, 1);
        holidays.setDayExcluded(nationalDay, true);

        scheduler.addCalendar("holidays", holidays, false, false);//註冊日期

        Date runDate = new Date();
        JobDetail job = new JobDetailImpl("job1", "group1", SimpleJob.class);
        SimpleTrigger trigger = new SimpleTriggerImpl(//
                "trigger1", "group1", runDate, null, //
                SimpleTrigger.REPEAT_INDEFINITELY, 60L * 60L * 1000L);
        scheduler.scheduleJob(job, trigger);
        scheduler.start();
    }

}
