package {{ cookiecutter.group }}.common_android.util

import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.data.TestData
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.options.UiAutomator2Options
import org.apache.logging.log4j.kotlin.Logging
import java.net.MalformedURLException
import java.net.URI
import kotlin.test.assertNotEquals

class AndroidDriverProvider : Logging {
    companion object : Logging {
        fun createDriver(
            server: String = SPConfig.selenideConfig.remote(),
            appUrl: String = TestData.getString("project.app_url") ?: "",
            appPackage: String? = TestData.getString("project.app_package"),
            appActivity: String? = TestData.getString("project.app_activity"),
        ): AndroidDriver {
            assertNotEquals("", appUrl, "App URL must be defined")
            val options = UiAutomator2Options().setApp(appUrl)
            appPackage?.let { options.setAppPackage(it) }
            appActivity?.let { options.setAppActivity(it) }

            logger.info("Server: $server\nCapabilities: \n$options")
            return try {
                val url = URI(server).toURL()
                AndroidDriver(url, options)
            } catch (e: MalformedURLException) {
                throw RuntimeException(e)
            }
        }
    }
}
