# IPLocatorSlackBot
Slackbot for getting information about IP-Addresses, Domains, ASNs and TLDs using my own IP Location API: https://github.com/Nils1024/IPLocatorAPI

### Commands
- /ipl-help
- /ip-lookup
- /domain-lookup
- /tld-lookup
- /asn-lookup

### Environment Variables
- SLACK_BOT_TOKEN
- SLACK_APP_TOKEN

### How to deploy?
1. Create a Slack Application
2. Run the maven build command:
```bash
mvn clean package -DskipTests
```
3. Copy the jar from target/ to your server
4. Add the jar as a service for autostart
```bash
sudo nano /etc/systemd/system/iplocatorslackbot.service
```
```
[Unit]
Description=IP Locator Slack Bot
After=network.target

[Service]
User=user
WorkingDirectory=/home/user/slackbot

Environment=SLACK_BOT_TOKEN=xoxb-...
Environment=SLACK_APP_TOKEN=xapp-...

ExecStart=/usr/bin/java -jar /home/user/slackbot/slack-bot-1.0.jar

Restart=always
RestartSec=5

[Install]
WantedBy=multi-user.target
```
5. Activate, enable and start the service:
```bash
sudo systemctl daemon-reload
sudo systemctl enable iplocatorslackbot
sudo systemctl start iplocatorslackbot
```