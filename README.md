hipchat-sns-bridge
=================================

To get your AWS (Auto-scaling, EC2, Elastic Beanstalk, etc.) notifications into a Hipchat room, you need an SNS topic and some sort of subscriber.
This app will act as a subscriber, adding a little sugar before calling hipchat, such as adding colour etc.

# Deployment
Deployed using [Ionroller](https://github.com/gilt/ionroller). You need an application secret as an environment variable, otherwise it's just a simple Play app wrapped in a Docker container.

# Setup
- Create an SNS topic
- Create a HTTP(s) subscription to the topic
    - e.g. `http://<server url>/hipchat/<room id>?api_token=<room notification api token>`
- Add SNS topic notification to your auto-scaling groups.
