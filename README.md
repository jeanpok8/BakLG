# BakLG(M7019E course's repository.)

This repository contains a Home Automation project, with the purpose of helping the elderly and /or disabled persons to remain home safe and comfortable, rather than moving to a costly healthcare facility. The system makes it possible for family members to monitor their loved ones from anywhere with an internet connection using very simple alerts ( Pushbullet ). To achieve that, the system helps to regulate the home environment ( lighting, appliances,...) and generate alerts automatically if significant changes are observed in the user’s vital signs.
  
The project is mainly built based on OpenHAB which is a vandor and technology agnostic open source automation
software for home automation. For quick a setup, please visit https://github.com/openhab/openhab/wiki/Quick-Setup-an-openHAB-Server. For this project data are persisted in MySQL database, to govern the behaviour of that home automation and context reasoning, the rules are written out of Openhab using Java programming language. The communication between the application written in Java and Openhab follows Rest. for further information, please check the project Architecture.
PROJECT ARCHITECTURE

![screenshot from 2015-06-20 12 51 37](https://cloud.githubusercontent.com/assets/8640427/8266996/d934fc6e-174c-11e5-9857-352d4e25ce80.png)




