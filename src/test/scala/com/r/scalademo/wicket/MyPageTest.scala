package com.r.scalademo.wicket

import org.junit.Test
import org.apache.wicket.util.tester.WicketTester

@Test
class MyPageTest {

  @Test
  def testRender = {
    val tester = new WicketTester( new WicketScalaApplication)
    tester.startPage(classOf[MyPage])
    tester.assertRenderedPage(classOf[MyPage])
    tester.assertVisible("helloWorldLabel")
  }

}
