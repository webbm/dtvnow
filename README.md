
### DirecTV NOW Notifier  

This runs as a cron job and checks various AppleTV apps (channels) to see if they support DirecTV NOW
as a sign-in provider. If any do, a slack notification is sent to me. The results are then saved.

The slack message is sent via a shell command.

##### Usage
```bash
java -Dfile.location=/path/to/file -Dnotify.slack.channel=@slackChannel -jar /path/to/jar
```

##### Requirements
Java 8 is required to run the jar. Maven 3 is required to build the project.

##### VM Options
* `file.location`
 * Required
 * This is the location of the data file
 * This will be overwritten with new data each time the program runs.
* `notify.slack.channel`  
 * Required
 * The name of the slack channel to which to post the message.

##### Data File Schema

The data _must_ follow the following schema:
```json
[
    {
        "name"    : "The name of the app. This is free-form.",
        "url"     : "Where to get the available sign-in providers for this app.",
        "present" : "A Boolean indicating whether or not this app supports DirecTV NOW as a sign-in provider."
    }
]
```

Example
Note: _Disney Junior is actually supported as a DirecTV NOW provider. It's just listed as false to make sure the app does the correct thing during it's initial run._  
```json
[
    {
        "name": "FX",
        "url": "https://sp.auth.adobe.com/adobe-services/config/fx?noflash=true",
        "present": false
    },
    {
        "name": "AMC",
        "url": "https://api.auth.adobe.com/api/v1/config/AMC.json",
        "present": false
    },
    {
        "name": "FOX Sports GO",
        "url": "https://fsg.bamcontent.com/epg-files/mvpd/mvpd_data.json",
        "present": false
    },
    {
        "name": "TNT",
        "url": "https://api.auth.adobe.com/api/v1/config/TNT.json",
        "present": false
    },
    {
        "name": "TBS",
        "url": "https://api.auth.adobe.com/api/v1/config/TBS.json",
        "present": false
    },
    {
        "name": "HGTV",
        "url": "https://api.auth.adobe.com/api/v1/config/HGTV.json",
        "present": false
    },
    {
        "name": "Disney Junior",
        "url": "https://api.auth.adobe.com/api/v1/config/DisneyJunior?format=json",
        "present": false
    }
]
```
