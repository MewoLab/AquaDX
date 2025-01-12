package icu.samnyan.aqua.net.components

import ext.Bool
import ext.Str
import ext.logger
import icu.samnyan.aqua.net.db.AquaNetUser
import icu.samnyan.aqua.net.db.EmailConfirmation
import icu.samnyan.aqua.net.db.EmailConfirmationRepo
import org.simplejavamail.api.mailer.Mailer
import org.simplejavamail.email.EmailBuilder
import org.simplejavamail.springsupport.SimpleJavaMailSpringSupport
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.*

@Configuration
@ConfigurationProperties(prefix = "aqua-net.email")
class EmailProperties {
    var enable: Bool = false
    var senderName: Str = "AquaDX"
    var senderAddr: Str = "aquadx@example.com"
    var webHost: Str = "aquadx.net"
}

/**
 * Email service. All email related operations should be placed here.
 *
 * Library Documentation: https://www.simplejavamail.org/
 */
@Service
@Import(SimpleJavaMailSpringSupport::class)
class EmailService(
    val mailer: Mailer,
    val props: EmailProperties,
    val confirmationRepo: EmailConfirmationRepo,
) {
    val log = logger()
    val confirmTemplate: Str = this::class.java.getResource("/email/confirm.html")?.readText()
        ?: throw Exception("Email Template Not Found")

    @Async
    @EventListener(ApplicationStartedEvent::class)
    fun test() {
        if (!props.enable) return

        try {
            mailer.testConnection()
            log.info("Email Service Connected")
        } catch (e: Exception) {
            log.error("Email Service Connection Failed", e)
            throw e
        }
    }

    /**
     * Send a confirmation email to the user
     */
    fun sendConfirmation(user: AquaNetUser) {
        if (!props.enable) return

        // Generate token (UUID4)
        val token = UUID.randomUUID().toString()
        val confirmation = EmailConfirmation(token = token, aquaNetUser = user, createdAt = Date().toInstant())
        confirmationRepo.save(confirmation)

        // Send email
        log.info("Sending confirmation email to ${user.email}")
        mailer.sendMail(EmailBuilder.startingBlank()
            .from(props.senderName, props.senderAddr)
            .to(user.computedName, user.email)
            .withSubject("Confirm Your Email Address for AquaNet")
            .withHTMLText(confirmTemplate
                .replace("{{name}}", user.computedName)
                .replace("{{url}}", "https://${props.webHost}?confirm-email=$token"))
            .buildEmail()).thenRun { log.info("Confirmation email sent to ${user.email}") }
    }

    fun testEmail(addr: Str, name: Str) {
        if (!props.enable) return

        log.info("Sending test email to $addr")
        mailer.sendMail(EmailBuilder.startingBlank()
            .from(props.senderName, props.senderAddr)
            .to(name, addr)
            .withSubject("Test Email")
            .withPlainText("This is a test email to check if AquaNet Email Works").buildEmail()).thenRun {
                log.info("Test email sent to $addr")

        }
    }
}