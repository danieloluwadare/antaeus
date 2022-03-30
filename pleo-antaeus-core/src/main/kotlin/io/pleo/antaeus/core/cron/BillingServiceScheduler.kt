package io.pleo.antaeus.core.cron

import io.pleo.antaeus.core.services.BillingService
import mu.KotlinLogging
import org.quartz.*
import org.quartz.impl.StdSchedulerFactory

class BillingServiceScheduler(private val billingService: BillingService) {
    private val logger = KotlinLogging.logger { }

    private val scheduler: Scheduler = StdSchedulerFactory.getDefaultScheduler()

    init {
        scheduler.context["billingService"] = billingService
        scheduler.start()
        logger.info { "Scheduler started..." }
    }

    /**
     * Create job and trigger and schedule it to run on the first day of each month at 8am.
     */
    fun schedule() {
        logger.info { ">>>>>>>>> Invoices job scheduled. >>>>>>>>>>>>>" }

        val jobDetail = createJobDetail()
        jobDetail.jobDataMap["billingService"] = billingService

        val cronTrigger = createTrigger()

        scheduler.scheduleJob(jobDetail, cronTrigger)
        logger.info { "<<<<<<<<<<< Invoices job scheduled.<<<<<<<<<<<<<<" }
    }

    private fun createJobDetail(): JobDetail {
        return JobBuilder.newJob()
            .ofType(BillingServiceJob::class.java)
            .withIdentity("invoices")
            .build()
    }

    private fun createTrigger(): Trigger {
        return TriggerBuilder.newTrigger()

//            .withSchedule(CronScheduleBuilder.cronSchedule("0 * * ? * *")) //for testing every minute * * * * *
//            .withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * 1/1 * ? *")) //for testing * * * * *
            .withSchedule(CronScheduleBuilder.cronSchedule("0 0 9 1 1/1 ? *")) // for once per month  * * * * *
//
            .build()
    }
}