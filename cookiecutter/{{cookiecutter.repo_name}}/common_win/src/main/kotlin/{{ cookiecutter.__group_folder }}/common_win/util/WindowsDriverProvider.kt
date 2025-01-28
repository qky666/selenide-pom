package {{ cookiecutter.group }}.common_win.util

import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.data.TestData
import io.appium.java_client.windows.WindowsDriver
import io.appium.java_client.windows.options.WindowsOptions
import org.apache.logging.log4j.kotlin.Logging
import java.net.MalformedURLException
import java.net.URI


@Suppress("unused")
class WindowsDriverProvider : Logging {
    companion object : Logging {
        private const val DEFAULT_SERVER = "http://127.0.0.1:5000"

        fun createDriverForApp(
            server: String = SPConfig.selenideConfig.remote() ?: DEFAULT_SERVER,
            aumidOrPath: String = TestData.getString("project.app_aumid") ?: TestData.getString("project.app_path")
            ?: "Root",
            appArguments: String = TestData.getString("project.app_args") ?: "",
            appWorkingDir: String = TestData.getString("project.app_working_dir") ?: ""
        ): WindowsDriver {
            val options = WindowsOptions().setAutomationName("FlaUI").setApp(aumidOrPath)
            if (appArguments.isNotEmpty()) options.setAppArguments(appArguments)
            if (appWorkingDir.isNotEmpty()) options.setAppWorkingDir(appWorkingDir)
            // capabilities.setCapability("appium:deviceName", "WindowsPC")
            logger.info("Capabilities: \n$options")
            return try {
                val url = URI(server).toURL()
                WindowsDriver(url, options)
            } catch (e: MalformedURLException) {
                throw RuntimeException(e)
            }
        }

        fun createDriverForWindowHandle(
            server: String = SPConfig.selenideConfig.remote() ?: DEFAULT_SERVER,
            handle: String = TestData.getString("project.window_handle") ?: ""
        ): WindowsDriver {
            val options = WindowsOptions().setAutomationName("FlaUI").setAppTopLevelWindow(handle)
            // windowsOptions.setCapability("appium:deviceName", "WindowsPC")
            logger.info("Capabilities: \n$options")
            return try {
                val url = URI(server).toURL()
                WindowsDriver(url, options)
            } catch (e: MalformedURLException) {
                throw RuntimeException(e)
            }
        }

        fun createDriverForWindowName(
            server: String = SPConfig.selenideConfig.remote() ?: DEFAULT_SERVER,
            name: String = TestData.getString("project.window_name") ?: ""
        ): WindowsDriver {
            val options = WindowsOptions().setAutomationName("FlaUI")
            options.setCapability("appium:appTopLevelWindowTitleMatch", name)
            // windowsOptions.setCapability("appium:deviceName", "WindowsPC")
            logger.info("Capabilities: \n$options")
            return try {
                val url = URI(server).toURL()
                WindowsDriver(url, options)
            } catch (e: MalformedURLException) {
                throw RuntimeException(e)
            }
        }
    }
}
