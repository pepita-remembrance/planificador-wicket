package com.r.scalademo.wicket

import org.apache.wicket.protocol.http.WebApplication

class WicketScalaApplication extends WebApplication {

  override def getHomePage = classOf[MyPage]
}
