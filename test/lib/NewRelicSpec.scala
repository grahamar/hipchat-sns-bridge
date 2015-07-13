package lib

import org.scalatestplus.play.PlaySpec

class NewRelicSpec extends PlaySpec {

  "HipchatBridge" must {
    "recognise a new deployment" in {
      val msg = """Timestamp: Mon Jul 13 11:09:17 UTC 2015
                  |Message: Successfully launched environment: some-env-0-9-3-uN9
                  |
                  |Environment: some-env-0-9-3-uN9
                  |Application: some-app-name
                  |
                  |Environment URL: http://some-env-0-9-3-uN9.elasticbeanstalk.com
                  |RequestId: blah-blah-11e5-blah-blahblah
                  |NotificationProcessId: blah-blah-11e5-blah-blahblah""".stripMargin
      msg match {
        case NewRelic.NewEnvironmentLaunchedRegex(envName, appName) =>
          appName mustEqual "some-app-name"
          envName mustEqual "some-env-0-9-3-uN9"

        case _ => fail()
      }
    }
  }

}
