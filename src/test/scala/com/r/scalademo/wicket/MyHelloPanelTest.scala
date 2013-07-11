package com.r.scalademo.wicket

import org.junit.Test
import org.apache.wicket.util.tester.WicketTester

@Test
class MyHelloPanelTest {

  @Test
  def testRender = {
    val tester = new WicketTester( new WicketScalaApplication)
    tester.startComponentInPage(classOf[MyHelloPanel])
    tester.assertVisible("inputField")
  }

}
