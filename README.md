# ShadowRewrite [![Build](https://img.shields.io/teamcity/https/ci.bhop.me/s/yourmcgeek_ShadowRewrite.png?label=ShadowRewrite&?style=for-the-badge&colorA=191919&colorB=31b231&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAOCAMAAAAolt3jAAABrVBMVEUAAABQQUlTTlDSrf8+PDuY/f/KpP+keP+jef/No/9v/4pg+H+T/63Lnv+UZf+EYv59aP9/bP99ofI4+2A69VxM/3Ogb/8lz89C/4K6iv+RZP8AGxQAAAACAACSZ/8ABgwa0P9c6f+mdP+Jbv8Svv+S7P/bt/+Fe/8EsvhCxPlc5f8M0/QDsvhGxfmb7f8RwPUQvv9o3f8+w/gGtPMAEBYY0/893/9DxfkFsfQADhQAAAAAAAAHAwJl3f8Ouv4Hwv8Qyv8r0P8/Pz48Ojgw0v8KuP4EsvYOvP4lzP87z/9OyPlAxPk3zP+EZ/93c/9rff9ihf8Z6LUs85J+b/8bIEsBCB4ADxoAGxcAFh4AHB19dP8bHi6/vrPV0s5mX16vp6jWztA9NDQGBRsKCQOuraxWVVV1dHQAAAAACxEIw/8AFxgTEAy4uLc6Ojq+vr5EQ0MwKicAChALwP8AGhwFAABIR0dPT0+Ih4cRCwgACxAIyP0AFR0ADhQLxP8Iwv8NIiqXioablJJ+eHYJvP8AIzAABQYACAoACg0ACA4It/oIuv0Jvv8Hwf8Dsfb///+wb3fXAAAATXRSTlMAAAAAAAACGRoCCx0CC6Tz9bFD0Pd6hv3TAs3unwS49HAIVP6pAwbW+ikR0/0xAqK/Bh31+ZMSKPz53LgECNH8szQHBjvh/MBCEDktBTzDNREAAACQSURBVAjXNcexSoJhGAbQ5/S/fvoX0dQgTXYBrkFLDRGJVxs1FNEQREG3IS3RUgQSgThoZzuShuUuP0kq+9D7b3+C11MvrFJjDed0RxbZ+ajhxmXXTSLHzkB4UCkjW6VSer4P8Ftqau4z+UoO90qVMuY9udPeamAAk2tDz6mm4caIp0QuXN3z+Jckkcxwm4013d8Y2JqfGdEAAAAASUVORK5CYII=)](https://ci.bhop.me/viewType.html?buildTypeId=yourmcgeek_ShadowRewrite)

ShadowRewrite is a rewrite of the Discord bot used in the community Discord server for [ShadowNode](https://shadownode.ca). The rewrite modernizes the bot with the use of embeds, file uploaders, and private messaging. 

## Installation

To install, visit the [TeamCity](https://ci.bhop.me/viewType.html?buildTypeId=yourmcgeek_ShadowRewrite&tab=buildTypeStatusDiv&state=successful) project. All releases are built using [TeamCity](https://www.jetbrains.com/teamcity/)

## Usage

To use, download the jar file, and create the following script and run it in the same directory as the jar file. 


```bat
@echo off
java -jar ShadowRewrite-1.0-all.jar
pause
```
## Setup
After running it for the first time, edit the `config.json` file with the needed information. All fields are required in order for the bot to run successfully. To get your token, create a Discord application [here](https://discordapp.com/developers/applications).  After creating the application, turn it into a bot account by clicking in the following locations.

![Bot Settings](https://i.yourmcgeek.ga/f9jg5.png 'Bot Settings Location')
![alt text](https://i.yourmcgeek.ga/ak5q3.png 'Build a bot')

From there, copy your token and add it to the `config.json`. Be mindful never to share your token with anyone! You'll need to invite the bot to your guild(s) of choice. To do so, copy the client id found under `General Information` in the Settings panel, and replace it in the following URL.
```
https://discordapp.com/oauth2/authorize?client_id=INSERT_CLIENT_ID_HERE&scope=bot&permissions=8
```
In order for the bot to run, you will need to have [2-Factor Authentication](https://support.discordapp.com/hc/en-us/articles/219576828-Setting-up-Two-Factor-Authentication) enabled.

Once the bot is properly running, the following category and channels will need to be created. 
```
Support
├── support
├── supportlogs
```
Also, the following commands will have to be run in certain channels. Inside of your `support` channel, run the command `/supportsetup`. If you've changed your prefix, replace the `/` with your prefix of choice. Inside of the `supportlogs` channel, run `/logchannel`. 

By this point, you have been sent 2 private messages from the bot, copy and paste the values to their respective places inside the `config.json`.

If you would like the bot to provide insights based on peoples chat, you'll need to create the following array in your `config.json` file.
```JSON
   "tips": [  
      {  
         "suggestion":"If you need help with an issue related to the plugin run the `<prefix>help` command!",
         "word":"help"
     },
     {  
         "suggestion":"This is a test!",
         "word":"test"
     }
   ]
```
Follow the format in order to add more words that the bot will respond to with the corresponding message. 

#### Place holders

|  Placeholder 	|                                  Value                                  	|
|:------------:	|  :-----------------------------------------------------------------------:	|
|    <forum>   	|                          https://shadownode.ca                          	|
|   <prefix>   	|                         Prefix from config.json                         	|
|    <wiki>    	|                     https://www.shadownode.ca/wiki/                     	|
|   <support>  	|       If you need support, create a new ticket by running <prefix>ticket    |
|   <tag>     	|                              Tags the user                                  |

## Credit
Portions of the bot have been taken from NetBans Supportbot. Credit has been given via README and comments directly inside the java files taken from the original source.

Original source can be viewed [here](https://github.com/netbans/supportbot). 

The bot is built on the public Discord API known as [JDA](https://github.com/DV8FromTheWorld/JDA) with the 3rd party library of [JDA-Utilities](https://github.com/JDA-Applications/JDA-Utilities)

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
[GPL 3.0](https://github.com/YourMCGeek/ShadowRewrite/blob/master/LICENSE)
