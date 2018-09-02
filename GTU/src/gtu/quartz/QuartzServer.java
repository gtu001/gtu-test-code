/*
 * Copyright 2004-2005 OpenSymphony
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 */

/*
 * Previously Copyright (c) 2001-2004 James House
 */
package gtu.quartz;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.listeners.SchedulerListenerSupport;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * <p>
 * Instantiates an instance of Quartz Scheduler as a stand-alone program, if the
 * scheduler is configured for RMI it will be made available.
 * </p>
 *
 * <p>
 * The main() method of this class currently accepts 0 or 1 arguemtns, if there
 * is an argument, and its value is <code>"console"</code>, then the program
 * will print a short message on the console (std-out) and wait for the user to
 * type "exit" - at which time the scheduler will be shutdown.
 * </p>
 *
 * <p>
 * Future versions of this server should allow additional configuration for
 * responding to scheduler events by allowing the user to specify
 * <code>{@link org.quartz.JobListener}</code>,
 * <code>{@link org.quartz.TriggerListener}</code> and
 * <code>{@link org.quartz.SchedulerListener}</code> classes.
 * </p>
 *
 * <p>
 * Please read the Quartz FAQ entries about RMI before asking questions in the
 * forums or mail-lists.
 * </p>
 */
public class QuartzServer extends SchedulerListenerSupport {
    public static final String APPLICATION_CONTEXT_KEY = "applicationContext";
    public final static String DEFAULT_SPRING_CONFIG = "beans.xml";
    public final static String BEAN_SCHEDULER_NAME = "scheduler";
    public final static String BEAN_DATASOURCE_NAME = "dataSource";
    public final static String BEAN_FACTORY = "beanfactory";
    private Scheduler sched = null;

    QuartzServer() {
    }

    public void serve(Scheduler schd, boolean console) throws Exception {
        sched = schd;
        System.out.println(sched.getSchedulerListeners().get(1).toString());
        StatusSchedulerListener schedulerListener = (StatusSchedulerListener) sched.getSchedulerListeners().get(1);
        schedulerListener.setScheduler(schd);
        try {
            Thread.sleep(3000l);
        } catch (Exception ignore) {
        }

        System.out.println("\n*** The scheduler successfully started.");

        if (console) {
            System.out.println("\n");
            System.out.println("The scheduler will now run until you type \"exit\"");
            System.out.println("   If it was configured to export itself via RMI,");
            System.out.println("   then other process may now use it.");

            BufferedReader rdr = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                System.out.print("Type 'exit' to shutdown the server: ");
                if ("exit".equals(rdr.readLine())) {
                    break;
                }
            }

            System.out.println("\n...Shutting down server...");

            sched.shutdown(true);
        }
    }

    public void schedulerError(String msg, SchedulerException cause) {
        System.err.println("*** " + msg);
        cause.printStackTrace();
    }

    public void schedulerShutdown() {
        System.out.println("\n*** The scheduler is now shutdown.");
        sched = null;
    }

    public static void main(String[] args) throws Exception {

        String springConfigFile = null;
        springConfigFile = System.getProperty("spring.configuration");
        if (null == springConfigFile) {
            springConfigFile = DEFAULT_SPRING_CONFIG;
        }

        try {
            // 不要使用ApplicationContext to error not serialException
            // GenericApplicationContext ctx = new GenericApplicationContext();
            // XmlBeanDefinitionReader xmlReader = new
            // XmlBeanDefinitionReader(ctx);
            // xmlReader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_NONE);
            // xmlReader.loadBeanDefinitions(new
            // FileSystemResource(springConfigFile));
            // ctx.refresh();
            Resource resource = new FileSystemResource(springConfigFile);
            BeanFactory beanFactory = new XmlBeanFactory(resource);

            Scheduler schd = (Scheduler) beanFactory.getBean(BEAN_SCHEDULER_NAME);
            schd.getContext().put(APPLICATION_CONTEXT_KEY, beanFactory);
            QuartzServer server = new QuartzServer();
            if (args.length == 0) {
                server.serve(schd, false);
            } else if (args.length == 1 && args[0].equalsIgnoreCase("console")) {
                server.serve(schd, true);

            } else {
                System.err.println("\nUsage: QuartzServer [console]");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
