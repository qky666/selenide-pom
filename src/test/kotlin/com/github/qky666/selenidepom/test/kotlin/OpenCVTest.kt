package com.github.qky666.selenidepom.test.kotlin

import com.github.qky666.selenidepom.config.SPConfig
import com.github.qky666.selenidepom.data.TestData
import com.sun.tools.javac.tree.TreeInfo.args
import org.bytedeco.javacpp.DoublePointer
import org.bytedeco.opencv.global.opencv_imgcodecs.imread
import org.bytedeco.opencv.opencv_core.Mat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class OpenCVTest {

    @BeforeEach
    fun beforeEach() {
        TestData.init()
        SPConfig.resetConfig()
    }

    @AfterEach
    fun afterEach() {
        // Maybe not needed, but here for safety
        SPConfig.quitDriver()
    }

    @Test
    fun testOpenCV() {

        //read in image default colors
        val sourceColor = imread("C:/Users/qky66/OneDrive/Escritorio/Source.png")
        val sourceGrey = Mat(sourceColor.size(), CV_8UC1)
        cvtColor(sourceColor, sourceGrey, COLOR_BGR2GRAY)

        //load in template in grey
        val template: Mat = imread(args[1], IMREAD_GRAYSCALE) //int = 0

        //Size for the result image
        val size: Size = Size(sourceGrey.cols() - template.cols() + 1, sourceGrey.rows() - template.rows() + 1)
        val result: Mat = Mat(size, CV_32FC1)
        matchTemplate(sourceGrey, template, result, TM_CCORR_NORMED)

        val minVal = DoublePointer()
        val maxVal = DoublePointer()
        val min: Point = Point()
        val max: Point = Point()
        minMaxLoc(result, minVal, maxVal, min, max, null)
        rectangle(sourceColor, Rect(max.x(), max.y(), template.cols(), template.rows()), randColor(), 2, 0, 0)

        imshow("Original marked", sourceColor)
        imshow("Ttemplate", template)
        imshow("Results matrix", result)
        waitKey(0)
        destroyAllWindows()
    }
}
